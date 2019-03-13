package com.mkielar.pwr.email.viewModel.network

import com.mkielar.pwr.email.model.EmailDetails
import io.reactivex.Single

interface EmailDetailsDownloader {
    fun fetch(emailId: Int): Single<EmailDetails>
}