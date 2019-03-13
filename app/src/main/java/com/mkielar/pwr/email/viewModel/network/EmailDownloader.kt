package com.mkielar.pwr.email.viewModel.network

import io.reactivex.Completable

interface EmailDownloader {
    fun fetch(): Completable
}