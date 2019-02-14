package com.mkielar.pwr

import android.annotation.SuppressLint
import android.content.Context
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

object JsosLoader {
    fun getSite(context: Context, url: String): Single<Document> = Single.create { emitter: SingleEmitter<Document> ->
        try {
            emitter.onSuccess(
                Jsoup.connect(url)
                    .cookie(
                        "JSOSSESSID",
                        getJsosSessionId(context).subscribeOn(AndroidSchedulers.mainThread()).blockingGet()
                    )
                    .method(Connection.Method.GET)
                    .execute()
                    .parse()
            )
        } catch (e: java.lang.Exception) {
            emitter.onError(e)
        }
    }

    private fun getJsosSessionId(context: Context): Single<String> = Single.create { emitter ->
        try {
            val inputStream = context.assets.open("jsosCredentialsInjection.js")
            val inputString: String = inputStream.bufferedReader().use { it.readText() }
            val js = String.format(inputString, "login", "password")

            val webView = WebView(context)
            @SuppressLint("SetJavaScriptEnabled")
            webView.settings.javaScriptEnabled = true
            var temp = true
            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    if (temp && url.startsWith("https://oauth.pwr.edu.pl/")) {
                        val javaScript = "javascript:(function() {$js})()"
                        webView.loadUrl(javaScript)
                        temp = !temp
                    } else if (url.startsWith("https://jsos.pwr.edu.pl/")) {
                        emitter.onSuccess(CookieManager.getInstance()
                            .getCookie("jsos.pwr.edu.pl")
                            .split(" ")
                            .last { it.contains("JSOSSESSID") }
                            .split("=")
                            .last()
                            .trimEnd(';'))
                    }
                }
            }
            webView.loadUrl("https://jsos.pwr.edu.pl/index.php/site/loginAsStudent")
        } catch (e: java.lang.Exception) {
            emitter.onError(e)
        }
    }
}