package com.mkielar.pwr.email.details.viewmodel

import androidx.lifecycle.ViewModel
import com.mkielar.pwr.credentials.InvalidSessionException
import com.mkielar.pwr.email.api.network.EmailAuthenticator
import com.mkielar.pwr.email.api.network.EmailDetailsDownloader
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class DetailsViewModel(
    private val emailDetailsDownloader: EmailDetailsDownloader,
    private val emailAuthenticator: EmailAuthenticator
) : ViewModel(), Lifecycle.ViewModel {
    private var viewCallback: Lifecycle.View? = null
    private var disposable: Disposable? = null

    override fun requestEmailDetails(emailId: Int) {
        disposable?.dispose()
        disposable = emailDetailsDownloader.fetch(emailId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                viewCallback?.onEmailDetailsReceived(it)
            }, {
                if (it is InvalidSessionException) {
                    emailAuthenticator.reauth()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            requestEmailDetails(emailId)
                        }, {
                            viewCallback?.onEmailRequestFailed()
                        })
                } else {
                    viewCallback?.onEmailRequestFailed()
                }
            })
    }

    override fun onViewAttached(viewCallback: Lifecycle.View) {
        this.viewCallback = viewCallback
    }

    override fun onViewDetached() {
        this.viewCallback = null
    }

    override fun onCleared() {
        disposable?.dispose()
    }
}