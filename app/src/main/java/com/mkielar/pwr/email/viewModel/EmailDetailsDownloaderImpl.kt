package com.mkielar.pwr.email.viewModel

import com.mkielar.pwr.credentials.CredentialsStore
import com.mkielar.pwr.email.model.EmailDetails
import io.reactivex.Single
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.koin.standalone.KoinComponent

class EmailDetailsDownloaderImpl(
    private val credentialsStore: CredentialsStore,
    private val emailDetailsParser: EmailDetailsParser
) : KoinComponent, EmailDetailsDownloader {
    override fun fetch(emailId: Int): Single<EmailDetails> = Single.create<EmailDetails> {
        val jsessionid = credentialsStore.getJsessionid()
        val appToken = credentialsStore.getAppToken()

        val execute =
            Jsoup.connect("https://s.student.pwr.edu.pl/iwc/svc/wmap/msg.mjs?rev=3&sid=&mbox=INBOX&uid=$emailId&process=html%2Cjs%2Ctarget%2Cbinhex%2Clink&security=false&lang=pl&token=$appToken&dojo.preventCache=1551875557460")
                .cookie("JSESSIONID", jsessionid)
                .method(Connection.Method.GET)
                .execute()

        val emailDetails = emailDetailsParser.parse(execute.body())
        it.onSuccess(emailDetails)
    }
}