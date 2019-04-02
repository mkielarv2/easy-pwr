package com.mkielar.pwr.main.viewmodel

import androidx.lifecycle.ViewModel
import com.mkielar.pwr.credentials.CredentialsStore
import com.mkielar.pwr.credentials.CredentialsStoreImpl
import com.mkielar.pwr.credentials.Keys
import com.mkielar.pwr.email.api.network.EmailDownloader
import com.mkielar.pwr.jsos.api.network.JsosEmailDownloader
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainViewModel(
    private val jsosEmailDownloader: JsosEmailDownloader,
    private val emailDownloader: EmailDownloader,
    private val credentialsStore: CredentialsStore
) : ViewModel(), Lifecycle.ViewModel {
    private val disposables = mutableListOf<Disposable>()
    private var viewCallback: Lifecycle.View? = null

    override fun onViewAttached(viewCallback: Lifecycle.View) {
        this.viewCallback = viewCallback
        if (credentialsMissing()) {
            viewCallback.onCredentialsMissing()
        } else {
            startDownloaders()
        }
    }

    override fun onViewDetached() {
        viewCallback = null
        disposables.forEach { it.dispose() }
    }

    private fun credentialsMissing() = listOf(
        Keys.JSOS_LOGIN,
        Keys.JSOS_PASSWORD,
        Keys.STUDENT_LOGIN,
        Keys.STUDENT_PASSWORD
    ).none { !credentialsStore.hasValue(it) }

    private fun startDownloaders() {
        val downloaders = listOf(jsosEmailDownloader.fetch(), emailDownloader.fetch())
        downloaders.forEach {
            disposables.add(
                it.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        //ignored for now, some functionality may be added later on
                    }, { e ->
                        e.printStackTrace()
                    })
            )
        }
    }
}
