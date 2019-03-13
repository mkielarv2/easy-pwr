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
import com.google.android.material.snackbar.Snackbar
import com.mkielar.pwr.R
import com.mkielar.pwr.email.viewModel.EmailViewModel
import com.mkielar.pwr.email.viewModel.Lifecycle
import kotlinx.android.synthetic.main.fragment_email.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class EmailFragment : Fragment(), Lifecycle.View {
    private val viewModel: EmailViewModel by sharedViewModel()

    private val emailRecyclerAdapter = EmailRecyclerAdapter { index ->
        val action = EmailFragmentDirections.actionEmailFragmentToEmailDetailsActivity(index)
        findNavController().navigate(action)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_email, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        viewModel.onViewAttached(this)

        swipeRefresh.setOnRefreshListener {
            viewModel.requestDatabaseRefresh()
        }

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = emailRecyclerAdapter

        viewModel.emailLiveData.observe(this, Observer {
            emailRecyclerAdapter.setData(it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.email_menu, menu)

        activity?.run {
            val searchItem = menu.findItem(R.id.action_search)
            val searchManager = this.getSystemService(Context.SEARCH_SERVICE) as SearchManager

            val searchView = searchItem.actionView as SearchView
            searchView.setSearchableInfo(searchManager.getSearchableInfo(this.componentName))
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

    override fun onRefreshComplete() {
        swipeRefresh.isRefreshing = false
    }

    override fun onRefreshFailed() {
        swipeRefresh.isRefreshing = false
        Snackbar.make(coordinator, "Refresh failed, try again later", Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        viewModel.onViewDetached()
        super.onDestroyView()
    }
}
