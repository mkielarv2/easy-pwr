package com.mkielar.pwr.email.inbox.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mkielar.pwr.credentials.InvalidSessionException
import com.mkielar.pwr.database.AppDatabase
import com.mkielar.pwr.email.api.network.EmailAuthenticator
import com.mkielar.pwr.email.api.network.EmailDownloader
import com.mkielar.pwr.email.inbox.model.Email
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class EmailViewModel(
    appDatabase: AppDatabase,
    private val emailAuthenticator: EmailAuthenticator,
    private val emailDownloader: EmailDownloader
) : ViewModel(),
    Lifecycle.ViewModel {
    private var viewCallback: Lifecycle.View? = null
    private var disposable: Disposable? = null

    val emailLiveData: LiveData<List<Email>> = appDatabase.emailDao().getEmails()

    fun requestDatabaseRefresh() {
        disposable?.dispose()
        disposable = emailDownloader.fetch()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                viewCallback?.onRefreshComplete()
            }, {
                if (it is InvalidSessionException) {
                    emailAuthenticator.reauth()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            requestDatabaseRefresh()
                        }, { e ->
                            e.printStackTrace()
                            viewCallback?.onRefreshFailed()
                        })
                } else {
                    viewCallback?.onRefreshFailed()
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