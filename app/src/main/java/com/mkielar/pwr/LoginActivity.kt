package com.mkielar.pwr

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

    }

    fun submit(view: View) {
        val login = login_field.text.toString()
        val password = password_field.text.toString()

        if(login.isBlank() || password.isBlank()){
            Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        var disposable = JsosLoader.validateCredentials(this, login, password)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                CredentialsStore(this)
                    .putJsosCredentials(login, password)
            }, {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
            })
    }
}
