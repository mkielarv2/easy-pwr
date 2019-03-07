package com.mkielar.pwr.email.viewModel

import com.mkielar.pwr.credentials.CredentialsStore
import com.mkielar.pwr.database.AppDatabase
import com.mkielar.pwr.email.model.Email
import io.reactivex.Completable
import org.jsoup.Connection
import org.jsoup.Jsoup

class EmailDownloaderImpl(
    private val appDatabase: AppDatabase,
    private val credentialsStore: CredentialsStore,
    private val emailParser: EmailParser
) : EmailDownloader {
    override fun fetch(): Completable = Completable.create {
        val jsessionid = credentialsStore.getJsessionid()
        val appToken = credentialsStore.getAppToken()

        val execute = postRequest(appToken, jsessionid)

        val emails = emailParser.parse(execute.body())
        insertIntoDatabase(emails)
        it.onComplete()
    }

    private fun postRequest(appToken: String?, jsessionid: String?): Connection.Response =
        Jsoup.connect("https://s.student.pwr.edu.pl/iwc/svc/wmap/mbox.mjs?rev=3&sid=&mbox=INBOX&count=300&date=true&lang=pl&sortby=recv&sortorder=R&start=0&srch=UNDELETED&token=$appToken&dojo.preventCache=1551628616549")
            .cookie("JSESSIONID", jsessionid)
            .method(Connection.Method.GET)
            .execute()

    private fun insertIntoDatabase(emails: List<Email>) {
        appDatabase.emailDao().insertEmailList(emails)
    }
}