package com.mkielar.pwr.email.inbox.viewmodel

interface Lifecycle {
    interface View {
        fun onRefreshComplete()
        fun onRefreshFailed()
    }

    interface ViewModel {
        fun onViewAttached(viewCallback: View)
        fun onViewDetached()
    }
}