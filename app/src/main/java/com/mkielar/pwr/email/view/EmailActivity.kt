package com.mkielar.pwr.email.view

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
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

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title = "Student Mail"
        }

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.email_menu, menu)

        if (menu != null) {
            val searchItem = menu.findItem(R.id.action_search)
            val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

            val searchView = searchItem.actionView as SearchView
            searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    emailRecyclerAdapter.filter.filter(newText)
                    return true
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }
            })
        }

        return super.onCreateOptionsMenu(menu)
    }
}
