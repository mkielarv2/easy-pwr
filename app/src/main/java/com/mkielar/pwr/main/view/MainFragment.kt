package com.mkielar.pwr.main.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mkielar.pwr.R
import com.mkielar.pwr.main.viewmodel.Lifecycle
import com.mkielar.pwr.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MainFragment : Fragment(), Lifecycle.View {
    private val viewModel: MainViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.onViewAttached(this)
        loginButton.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }
    }

    override fun onCredentialsMissing() {
        findNavController().navigate(R.id.loginFragment)
    }

    override fun onDestroyView() {
        viewModel.onViewDetached()
        super.onDestroyView()
    }
}