package com.mkielar.pwr.jsos.api.network

import io.reactivex.Completable

interface JsosEmailDownloader {
    fun fetch(): Completable
}