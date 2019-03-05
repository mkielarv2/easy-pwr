package com.mkielar.pwr

import android.app.Application
import android.util.Log
import io.reactivex.Completable
import org.json.JSONObject
import org.jsoup.Connection
import org.jsoup.Jsoup


class EmailAuthenticator {
    fun login(application: Application, login: String, password: String): Completable = Completable.create { emitter ->
        val execute = Jsoup.connect("https://s.student.pwr.edu.pl/iwc/svc/iwcp/login.iwc")
            .method(Connection.Method.POST)
            .data("username", login)
            .data("password", password)
            .data("chkpreloginip", "true")
            .data("token", "badvalue")
            .data("fmt-out", "text/json")
            .execute()

        val response = JSONObject(execute.body()).getJSONObject("iwcp")

        if (response.getString("error-code") == "0") {
            val store = CredentialsStore(application)
            store.putCredentials(login, password)

            val loginResponse = response.getJSONObject("loginResponse")
            val jsessionid = loginResponse.getString("sessionIdValue")
            val token = loginResponse.getString("appToken").replace("token=", "")
            store.putTokens(jsessionid, token)

            Log.e("jsessionid", jsessionid)
            Log.e("token", token)

            emitter.onComplete()
        } else {
            emitter.onError(InvalidCredentialsException())
        }
    }
}