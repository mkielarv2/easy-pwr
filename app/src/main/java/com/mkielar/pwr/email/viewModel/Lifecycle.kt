package com.mkielar.pwr.email.viewModel

interface Lifecycle {
    interface View {
        fun onRefreshComplete()
        fun onRefreshFailed()
    }

    interface ViewModel {
        fun onViewAttached(viewCallback: Lifecycle.View)
        fun onViewDetached()
    }
}