package com.mkielar.pwr.credentials

interface CredentialsStore {
    fun putCredentials(login: String, password: String)
    fun getCredentials(): Pair<String?, String?>
    fun putTokens(jsessionid: String, appToken: String)
    fun getJsessionid(): String?
    fun getAppToken(): String?

    fun putJsosCredentials(login: String, password: String)
    fun getJsosCredentials(): Pair<String?, String?>
    fun putJsosSessionIdCookie(jsosSessionId: String)
    fun getJsosSessionIdCookie(): String?
}