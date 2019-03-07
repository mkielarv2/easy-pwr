package com.mkielar.pwr.email.viewModel

import io.reactivex.Completable

interface EmailAuthenticator {
    fun login(login: String, password: String): Completable
}