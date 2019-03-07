package com.mkielar.pwr.email.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mkielar.pwr.R
import com.mkielar.pwr.database.AppDatabase
import com.mkielar.pwr.email.viewModel.EmailDownloader
import com.mkielar.pwr.email.viewModel.EmailViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_email.*
import org.koin.android.ext.android.inject

class EmailActivity : AppCompatActivity() {
    private val appDatabase: AppDatabase by inject()
    private val emailDownloader: EmailDownloader by inject()

    private val emailRecyclerAdapter = EmailRecyclerAdapter { index ->
        val intent = Intent(this, EmailDetailsActivity::class.java)
        intent.putExtra(EmailDetailsActivity.EMAIL_ID_KEY, index)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email)

        swipeRefresh.setOnRefreshListener {
            emailDownloader.fetch()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe {
                    swipeRefresh.isRefreshing = false
                }
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = emailRecyclerAdapter

        EmailViewModel(appDatabase.emailDao()).emailLiveData.observe(this, Observer {
            emailRecyclerAdapter.setData(it)
        })
    }
}
