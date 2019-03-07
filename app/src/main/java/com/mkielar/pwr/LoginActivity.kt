package com.mkielar.pwr

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mkielar.pwr.credentials.InvalidCredentialsException
import com.mkielar.pwr.email.view.EmailActivity
import com.mkielar.pwr.email.viewModel.EmailAuthenticator
import com.mkielar.pwr.email.viewModel.EmailDownloader
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        email_button.setOnClickListener {
            startActivity(Intent(this, EmailActivity::class.java))
        }
    }

    fun submit(view: View) {
        val login = login_field.text.toString()
        val password = password_field.text.toString()

        if (login.isBlank() || password.isBlank()) {
            Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        var disposable = EmailAuthenticator().login(application, login, password)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show()
                val disposable = EmailDownloader(application).fetch()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe()
                startActivity(Intent(this, EmailActivity::class.java))
            }, {
                it.printStackTrace()
                if (it is InvalidCredentialsException)
                    Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
            })
    }
}
