package com.mkielar.pwr.main.viewmodel

interface Lifecycle {
    interface ViewModel {
        fun onViewAttached(viewCallback: View)
        fun onViewDetached()
    }

    interface View {
        fun onCredentialsMissing()
    }
}