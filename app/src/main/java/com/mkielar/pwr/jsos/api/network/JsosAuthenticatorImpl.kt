package com.mkielar.pwr.jsos.api.network

import com.mkielar.pwr.credentials.CredentialsStore
import com.mkielar.pwr.credentials.InvalidCredentialsException
import com.mkielar.pwr.credentials.MissingCredentialsException
import io.reactivex.Completable
import org.jsoup.Connection
import org.jsoup.Jsoup
import java.io.IOException
import java.util.regex.Pattern

class JsosAuthenticatorImpl(
    private val credentialsStore: CredentialsStore
) : JsosAuthenticator {
    private lateinit var url: String
    private lateinit var form: String
    private lateinit var token: String
    private lateinit var consumerKey: String

    private lateinit var jsosSessionIdCookie: String

    override fun login(username: String, password: String): Completable = Completable.create { emitter ->
        auth(username, password)
        credentialsStore.putJsosCredentials(username, password)
        credentialsStore.putJsosSessionIdCookie(jsosSessionIdCookie)
        emitter.onComplete()
    }

    override fun reauth(): Completable = Completable.create { emitter ->
        val (login, password) = credentialsStore.getJsosCredentials()
        if (login == null || password == null) throw MissingCredentialsException()
        auth(login, password)
        credentialsStore.putJsosSessionIdCookie(jsosSessionIdCookie)
        emitter.onComplete()
    }

    private fun auth(username: String, password: String) {
        getSessionCookie()
        getDetails()
        requestAuthentication(username, password)
    }

    private fun getSessionCookie() {
        val response = Jsoup.connect("https://jsos.pwr.edu.pl")
            .method(Connection.Method.GET)
            .execute()

        jsosSessionIdCookie = response.cookies().map { e -> "${e.key}=${e.value}" }[0]
    }

    private fun getDetails() {
        val response = Jsoup.connect("https://jsos.pwr.edu.pl/index.php/site/loginAsStudent")
            .header("Cookie", jsosSessionIdCookie)
            .method(Connection.Method.GET)
            .execute()
        val doc = response.parse()
        val inputs = doc.getElementsByTag("input")

        url = "https://oauth.pwr.edu.pl/oauth/" + doc.getElementsByClass("loginForm")[0].attr("action")
        form = inputs[0].attr("name")
        consumerKey = inputs[2].attr("value")
        token = inputs[3].attr("value")
    }

    private fun requestAuthentication(username: String, password: String) {
        val redirectUrl = getRedirect(username, password)

        val response = Jsoup.connect(redirectUrl)
            .header("Cookie", jsosSessionIdCookie)
            .method(Connection.Method.GET)
            .followRedirects(false)
            .execute()

        val pattern = Pattern.compile("JSOSSESSID=.{26}")
        val matcher = pattern.matcher(response.header("Set-Cookie"))
        if (matcher.find()) {
            jsosSessionIdCookie = matcher.group()
        } else {
            throw IOException("No JSOSSESSID cookie present in response")
        }
    }

    private fun getRedirect(username: String, password: String): String? {
        val response = Jsoup.connect(url)
            .data(form, "")
            .data("oauth_request_url", "http://oauth.pwr.edu.pl/oauth/authenticate")
            .data("oauth_consumer_key", consumerKey)
            .data("oauth_token", token)
            .data("oauth_locale", "pl")
            .data("oauth_callback_url", "https://jsos.pwr.edu.pl/index.php/site/loginAsStudent")
            .data("oauth_symbol", "EIS")
            .data("username", username)
            .data("password", password)
            .data("authenticateButton", "Zaloguj")
            .method(Connection.Method.POST)
            .followRedirects(false)
            .execute()

        if (response.statusCode() == 200) throw InvalidCredentialsException()
        return response.header("Location")
    }
}