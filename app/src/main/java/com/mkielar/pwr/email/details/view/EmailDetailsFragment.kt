package com.mkielar.pwr.email.details.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.mkielar.pwr.R
import com.mkielar.pwr.email.api.network.EmailDetailsDownloader
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_email_details.*
import org.koin.android.ext.android.inject

class EmailDetailsFragment : Fragment() {
    private val args: EmailDetailsFragmentArgs by navArgs()
    private val emailDetailsDownloader: EmailDetailsDownloader by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_email_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val emailId = args.emailId
        val disposable = emailDetailsDownloader.fetch(emailId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe { emailDetails ->
                webView.loadData(
                    "<html><head></head><body>${emailDetails.content}</body></html>",
                    "text/html",
                    "utf-8"
                )
            }
    }
}
