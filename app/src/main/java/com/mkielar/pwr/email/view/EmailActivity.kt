package com.mkielar.pwr.email.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mkielar.pwr.R
import com.mkielar.pwr.database.AppDatabase
import com.mkielar.pwr.email.viewModel.EmailViewModel
import kotlinx.android.synthetic.main.activity_email.*

class EmailActivity : AppCompatActivity() {

    private val emailRecyclerAdapter = EmailRecyclerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email)

//        swipeRefresh.setOnRefreshListener {
//
//        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = emailRecyclerAdapter

        val appDatabase = AppDatabase.getInstance(this)

        EmailViewModel(appDatabase.emailDao()).emailLiveData.observe(this, Observer {
            emailRecyclerAdapter.setData(it)
        })
    }
}
