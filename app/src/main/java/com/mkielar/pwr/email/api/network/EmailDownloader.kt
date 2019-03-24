package com.mkielar.pwr.email.api.network

import io.reactivex.Completable

interface EmailDownloader {
    fun fetch(): Completable
}