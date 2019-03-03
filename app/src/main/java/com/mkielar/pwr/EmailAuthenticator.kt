package com.mkielar.pwr

import io.reactivex.Completable
import org.json.JSONObject
import org.jsoup.Connection
import org.jsoup.Jsoup


class EmailAuthenticator {
    fun login(login: String, password: String): Completable = Completable.create { emitter ->
        val execute = Jsoup.connect("https://s.student.pwr.edu.pl/iwc/svc/iwcp/login.iwc")
            .method(Connection.Method.POST)
            .data("username", login)
            .data("password", password)
            .data("chkpreloginip", "true")
            .data("token", "badvalue")
            .data("fmt-out", "text/json")
            .execute()

        val response = JSONObject(execute.body()).getJSONObject("iwcp")

        if(response.getString("error-code") == "0") {
            //Todo store credentials and tokens
            emitter.onComplete()
        } else {
            emitter.onError(InvalidCredentialsException())
        }
    }
}