package com.mkielar.pwr.email.view

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mkielar.pwr.R
import com.mkielar.pwr.database.AppDatabase
import com.mkielar.pwr.email.viewModel.EmailDownloader
import com.mkielar.pwr.email.viewModel.EmailViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_email.*
import org.koin.android.ext.android.inject

class EmailFragment : Fragment() {
    private val appDatabase: AppDatabase by inject()
    private val emailDownloader: EmailDownloader by inject()

    private val emailRecyclerAdapter = EmailRecyclerAdapter { index ->
        val action = EmailFragmentDirections.actionEmailFragmentToEmailDetailsActivity(index)
        findNavController().navigate(action)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_email, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)

        swipeRefresh.setOnRefreshListener {
            emailDownloader.fetch()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe {
                    swipeRefresh.isRefreshing = false
                }
        }

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = emailRecyclerAdapter

        EmailViewModel(appDatabase.emailDao()).emailLiveData.observe(this, Observer {
            emailRecyclerAdapter.setData(it)
        })
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.email_menu, menu)

        val activity = activity
        if (activity != null) {
            val searchItem = menu.findItem(R.id.action_search)
            val searchManager = activity.getSystemService(Context.SEARCH_SERVICE) as SearchManager

            val searchView = searchItem.actionView as SearchView
            searchView.setSearchableInfo(searchManager.getSearchableInfo(activity.componentName))
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
    }
}
