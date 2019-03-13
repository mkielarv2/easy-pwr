package com.mkielar.pwr.email.details.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.mkielar.pwr.R
import com.mkielar.pwr.email.details.model.EmailDetails
import com.mkielar.pwr.email.details.viewmodel.DetailsViewModel
import com.mkielar.pwr.email.details.viewmodel.Lifecycle
import kotlinx.android.synthetic.main.fragment_email_details.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class EmailDetailsFragment : Fragment(), Lifecycle.View {
    private val args: EmailDetailsFragmentArgs by navArgs()
    private val viewModel: DetailsViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_email_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.onViewAttached(this)
        viewModel.requestEmailDetails(args.email.id)
    }

    override fun onEmailDetailsReceived(it: EmailDetails) {
        webView.loadData(it.content, "text/html", "utf-8")
    }

    override fun onEmailRequestFailed() {
        Snackbar.make(coordinator, "Download failed, try again later", Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        viewModel.onViewDetached()
        super.onDestroyView()
    }
}
