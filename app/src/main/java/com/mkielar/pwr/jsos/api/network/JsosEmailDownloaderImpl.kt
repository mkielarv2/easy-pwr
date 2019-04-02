package com.mkielar.pwr.jsos.api.network

import com.mkielar.pwr.credentials.CredentialsStore
import com.mkielar.pwr.credentials.CredentialsStoreImpl
import com.mkielar.pwr.credentials.Keys
import com.mkielar.pwr.database.AppDatabase
import io.reactivex.Completable
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.text.SimpleDateFormat
import java.util.*

class JsosEmailDownloaderImpl(
    private val credentialsStore: CredentialsStore,
    private val appDatabase: AppDatabase
) : JsosEmailDownloader {
    override fun fetch(): Completable = Completable.create { emitter ->
        val emails = mutableListOf<JsosEmail>()

        var i = 1
        while (true) {
            val response = Jsoup.connect("https://jsos.pwr.edu.pl/index.php/student/wiadomosci/$i")
                .header("Cookie", credentialsStore.getValue(Keys.JSOS_SESSION_ID))
                .method(Connection.Method.GET)
                .execute()
            val doc = response.parse()
            val messageTable = doc.getElementById("listaWiadomosci")
            val tableBody = messageTable.getElementsByTag("tbody").first()
            val rows = tableBody.getElementsByTag("tr")
            if (rows.size == 1 && rows.first().hasClass("emptyRow")) {
                //no more emails to parse
                break
            }

            rows.forEach {
                val subRows = it.getElementsByTag("td")
                emails.add(
                    JsosEmail(
                        parseDate(subRows).time,
                        parseUrl(it),
                        parseSender(subRows),
                        parseTitle(subRows)
                    )
                )
            }
            i++
        }
        appDatabase.jsosEmailDao().insertEmailList(emails)
        emitter.onComplete()
    }

    private fun parseDate(subRows: Elements) =
        SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ROOT).parse(subRows[3].wholeText())

    private fun parseUrl(it: Element) = "https://jsos.pwr.edu.pl${it.attr("data-url")}"

    private fun parseSender(subRows: Elements) = subRows[1].wholeText()

    private fun parseTitle(subRows: Elements) = subRows[2].wholeText()
}