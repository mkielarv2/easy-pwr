package com.mkielar.pwr.jsos.api.network

import io.reactivex.Completable

interface JsosAuthenticator {
    fun login(username: String, password: String): Completable
    fun reauth(): Completable
}