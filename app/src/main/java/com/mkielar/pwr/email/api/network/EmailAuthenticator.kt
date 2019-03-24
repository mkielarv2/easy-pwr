package com.mkielar.pwr.email.api.network

import io.reactivex.Completable

interface EmailAuthenticator {
    fun login(login: String, password: String): Completable
    fun reauth() : Completable
}