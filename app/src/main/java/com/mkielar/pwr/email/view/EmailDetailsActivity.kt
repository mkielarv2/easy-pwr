package com.mkielar.pwr.email.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mkielar.pwr.R
import com.mkielar.pwr.email.viewModel.EmailDetailsDownloader
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_email_details.*
import org.koin.android.ext.android.inject

class EmailDetailsActivity : AppCompatActivity() {
    private val emailDetailsDownloader: EmailDetailsDownloader by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_details)

        val extras = intent.extras
        if (extras != null) {
            val emailId = extras.getInt(EMAIL_ID_KEY)
            val disposable = emailDetailsDownloader.fetch(emailId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe { emailDetails ->
                    webView.loadData(
                        "<html><head></head><body>${emailDetails.content}</body></html>",
                        "text/html",
                        "utf-8"
                    )
                }
        }
    }

    companion object {
        const val EMAIL_ID_KEY = "email_id"
    }
}
