package com.mkielar.pwr.email.details.viewmodel

import com.mkielar.pwr.email.details.model.EmailDetails

interface Lifecycle {
    interface View {
        fun onEmailDetailsReceived(it: EmailDetails)
        fun onEmailRequestFailed()
    }

    interface ViewModel {
        fun requestEmailDetails(emailId: Int)
        fun onViewAttached(viewCallback: Lifecycle.View)
        fun onViewDetached()
    }
}