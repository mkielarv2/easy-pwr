package com.mkielar.pwr.email.viewModel

import com.mkielar.pwr.credentials.CredentialsStore
import com.mkielar.pwr.credentials.InvalidCredentialsException
import io.reactivex.Completable
import org.json.JSONObject
import org.jsoup.Connection
import org.jsoup.Jsoup


class EmailAuthenticatorImpl(private val credentialsStore: CredentialsStore) : EmailAuthenticator {
    override fun login(login: String, password: String): Completable = Completable.create { emitter ->
        val response = postRequest(login, password)

        val responseBody = JSONObject(response.body()).getJSONObject("iwcp")

        if (isLoginSuccessful(responseBody)) {
            storeCredentials(login, password)

            val loginResponse = responseBody.getJSONObject("loginResponse")
            val jsessionid = getJsessionid(loginResponse)
            val token = getToken(loginResponse)
            storeTokens(jsessionid, token)

            emitter.onComplete()
        } else {
            emitter.onError(InvalidCredentialsException())
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