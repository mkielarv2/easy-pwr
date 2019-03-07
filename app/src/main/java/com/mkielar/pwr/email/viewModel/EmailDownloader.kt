package com.mkielar.pwr.email.viewModel

import io.reactivex.Completable

interface EmailDownloader {
    fun fetch(): Completable
}