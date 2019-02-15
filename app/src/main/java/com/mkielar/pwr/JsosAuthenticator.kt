package com.mkielar.pwr

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

object JsosLoader {
    fun getSite(activity: Activity, url: String): Single<Document> = Single.create { emitter: SingleEmitter<Document> ->
        try {
            val (login, password) = CredentialsStore(activity).getJsosCredentials()
            if (login == null || password == null) {
                emitter.onError(java.lang.Exception("Missing Credentials"))
            } else {
                emitter.onSuccess(
                    Jsoup.connect(url)
                        .cookie(
                            "JSOSSESSID",
                            getJsosSessionId(activity, login, password)
                                .subscribeOn(AndroidSchedulers.mainThread())
                                .blockingGet()
                        )
                        .method(Connection.Method.GET)
                        .execute()
                        .parse()
                )
            }
        } catch (e: java.lang.Exception) {
            emitter.onError(e)
        }
    }

    fun validateCredentials(context: Context, login: String, password: String): Completable =
        Completable.create { emitter ->
            getJsosSessionId(context, login, password)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({ t -> emitter.onComplete() }, { emitter.onError(it) })
        }

    private fun getJsosSessionId(context: Context, login: String, password: String): Single<String> =
        Single.create { emitter ->
            try {
                val inputStream = context.assets.open("jsosCredentialsInjection.js")
                val inputString: String = inputStream.bufferedReader().use { it.readText() }
                val js = String.format(inputString, login, password)

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
                                .lastOrNull { it.contains("JSOSSESSID") }
                            !!.split("=")
                                .last()
                                .trimEnd(';'))
                        } else {
                            emitter.onError(Exception("fail"))
                        }
                    }
                }
                webView.loadUrl("https://jsos.pwr.edu.pl/index.php/site/loginAsStudent")
            } catch (e: java.lang.Exception) {
                emitter.onError(e)
            }
        }
}