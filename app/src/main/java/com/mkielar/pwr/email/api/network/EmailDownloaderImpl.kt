package com.mkielar.pwr.email.api.network

import com.mkielar.pwr.credentials.CredentialsStore
import com.mkielar.pwr.credentials.InvalidSessionException
import com.mkielar.pwr.database.AppDatabase
import com.mkielar.pwr.email.inbox.model.Email
import com.mkielar.pwr.email.api.parse.EmailParser
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

        val response = postRequest(appToken, jsessionid).body()

        if (isSessionInvalid(response)) throw InvalidSessionException()

        val emails = emailParser.parse(response)
        insertIntoDatabase(emails)
        it.onComplete()
    }

    private fun postRequest(appToken: String?, jsessionid: String?): Connection.Response =
        Jsoup.connect("https://s.student.pwr.edu.pl/iwc/svc/wmap/mbox.mjs?rev=3&sid=&mbox=INBOX&count=300&date=true&lang=pl&sortby=recv&sortorder=R&start=0&srch=UNDELETED&token=$appToken&dojo.preventCache=1551628616549")
            .cookie("JSESSIONID", jsessionid)
            .method(Connection.Method.GET)
            .execute()

    private fun isSessionInvalid(response: String) =
        response.replace("\\n", "").replace("while(1);", "").trim() == "[1101, 'Invalid Session']"

    private fun insertIntoDatabase(emails: List<Email>) {
        appDatabase.emailDao().insertEmailList(emails)
    }
}