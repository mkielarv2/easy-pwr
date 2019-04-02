package com.mkielar.pwr.email.api.network

import com.mkielar.pwr.credentials.CredentialsStore
import com.mkielar.pwr.credentials.CredentialsStoreImpl
import com.mkielar.pwr.credentials.InvalidSessionException
import com.mkielar.pwr.credentials.Keys
import com.mkielar.pwr.email.api.parse.EmailDetailsParser
import com.mkielar.pwr.email.details.model.EmailDetails
import io.reactivex.Single
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.koin.standalone.KoinComponent

class EmailDetailsDownloaderImpl(
    private val credentialsStore: CredentialsStore,
    private val emailDetailsParser: EmailDetailsParser
) : KoinComponent, EmailDetailsDownloader {
    override fun fetch(emailId: Int): Single<EmailDetails> = Single.create<EmailDetails> {
        val jsessionid = credentialsStore.getValue(Keys.STUDENT_JSESSIONID)
        val appToken = credentialsStore.getValue(Keys.STUDENT_APPTOKEN)

        val response = postRequest(emailId, appToken, jsessionid).body()

        if (isSessionInvalid(response)) throw InvalidSessionException()

        val emailDetails = emailDetailsParser.parse(response)
        it.onSuccess(emailDetails)
    }

    private fun postRequest(emailId: Int, appToken: String?, jsessionid: String?): Connection.Response =
        Jsoup.connect("https://s.student.pwr.edu.pl/iwc/svc/wmap/msg.mjs?rev=3&sid=&mbox=INBOX&uid=$emailId&process=html%2Cjs%2Ctarget%2Cbinhex%2Clink&security=false&lang=pl&token=$appToken&dojo.preventCache=1551875557460")
            .cookie("JSESSIONID", jsessionid)
            .method(Connection.Method.GET)
            .execute()

    private fun isSessionInvalid(response: String) =
        response.replace("\\n", "").replace("while(1);", "").trim() == "[1101, 'Invalid Session']"

}