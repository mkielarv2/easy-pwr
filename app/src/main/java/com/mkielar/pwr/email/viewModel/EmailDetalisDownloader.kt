package com.mkielar.pwr.email.viewModel

import android.app.Application
import com.mkielar.pwr.credentials.CredentialsStore
import com.mkielar.pwr.email.model.EmailDetails
import io.reactivex.Single
import org.jsoup.Connection
import org.jsoup.Jsoup

class EmailDetalisDownloader(private val application: Application) {
    fun fetch(emailId: Int) = Single.create<EmailDetails> {
        val credentialsStore = CredentialsStore(application)
        val jsessionid = credentialsStore.getJsessionid()
        val appToken = credentialsStore.getAppToken()

        val execute =
            Jsoup.connect("https://s.student.pwr.edu.pl/iwc/svc/wmap/msg.mjs?rev=3&sid=&mbox=INBOX&uid=$emailId&process=html%2Cjs%2Ctarget%2Cbinhex%2Clink&security=false&lang=pl&token=$appToken&dojo.preventCache=1551875557460")
                .cookie("JSESSIONID", jsessionid)
                .method(Connection.Method.GET)
                .execute()

        val emailDetails = EmailDetailsParser().parse(execute.body())
        it.onSuccess(emailDetails)
    }
}