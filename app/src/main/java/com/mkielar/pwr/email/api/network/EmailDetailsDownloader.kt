package com.mkielar.pwr.email.api.network

import com.mkielar.pwr.email.details.model.EmailDetails
import io.reactivex.Single

interface EmailDetailsDownloader {
    fun fetch(emailId: Int): Single<EmailDetails>
}