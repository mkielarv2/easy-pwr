package com.mkielar.pwr.email.api.network

import com.mkielar.pwr.credentials.CredentialsStore
import com.mkielar.pwr.credentials.InvalidCredentialsException
import com.mkielar.pwr.credentials.MissingCredentialsException
import io.reactivex.Completable
import org.json.JSONObject
import org.jsoup.Connection
import org.jsoup.Jsoup


class EmailAuthenticatorImpl(private val credentialsStore: CredentialsStore) :
    EmailAuthenticator {
    override fun login(login: String, password: String): Completable = Completable.create { emitter ->
        auth(login, password).subscribe({
            storeCredentials(login, password)
            emitter.onComplete()
        }, {
            emitter.onError(it)
        })
    }

    override fun reauth(): Completable = Completable.create { emitter ->
        val (login, password) = credentialsStore.getCredentials()
        if (login == null || password == null) throw MissingCredentialsException()
        auth(login, password).subscribe({
            emitter.onComplete()
        }, {
            emitter.onError(it)
        })
    }

    private fun auth(login: String, password: String) = Completable.create {
        val response = postRequest(login, password)

        val responseBody = JSONObject(response.body()).getJSONObject("iwcp")

        if (isLoginSuccessful(responseBody)) {
            val loginResponse = responseBody.getJSONObject("loginResponse")
            val jsessionid = getJsessionid(loginResponse)
            val token = getToken(loginResponse)
            storeTokens(jsessionid, token)

            it.onComplete()
        } else {
            it.onError(InvalidCredentialsException())
        }
    }

    private fun postRequest(login: String, password: String): Connection.Response =
        Jsoup.connect("https://s.student.pwr.edu.pl/iwc/svc/iwcp/login.iwc")
            .method(Connection.Method.POST)
            .data("username", login)
            .data("password", password)
            .data("chkpreloginip", "true")
            .data("token", "badvalue")
            .data("fmt-out", "text/json")
            .execute()

    private fun isLoginSuccessful(responseBody: JSONObject) = responseBody.getString("error-code") == "0"

    private fun storeCredentials(login: String, password: String) {
        credentialsStore.putCredentials(login, password)
    }

    private fun getJsessionid(loginResponse: JSONObject) = loginResponse.getString("sessionIdValue")

    private fun getToken(loginResponse: JSONObject) = loginResponse.getString("appToken").replace("token=", "")

    private fun storeTokens(jsessionid: String, token: String) {
        credentialsStore.putTokens(jsessionid, token)
    }
}