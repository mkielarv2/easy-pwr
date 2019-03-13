package com.mkielar.pwr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mkielar.pwr.credentials.InvalidCredentialsException
import com.mkielar.pwr.email.viewModel.network.EmailAuthenticator
import com.mkielar.pwr.email.viewModel.network.EmailDownloader
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.android.ext.android.inject

class LoginFragment : Fragment() {
    private val emailAuthenticator: EmailAuthenticator by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_login, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        submit.setOnClickListener(onSubmitClickListener)
        email_button.setOnClickListener {
            findNavController().navigate(R.id.emailFragment)
        }
    }

    private val onSubmitClickListener = View.OnClickListener {
        val login = login_field.text.toString()
        val password = password_field.text.toString()

        if (login.isBlank() || password.isBlank()) {
            Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            return@OnClickListener
        }

        var disposable = emailAuthenticator.login(login, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show()
                val emailDownloader: EmailDownloader by inject()
                val disposable = emailDownloader.fetch()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe()
                findNavController().navigate(R.id.emailFragment)
            }, {
                if (it is InvalidCredentialsException)
                    Toast.makeText(context, "Invalid credentials", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(context, "${it.message}", Toast.LENGTH_SHORT).show()
                it.printStackTrace()
            })
    }
}
