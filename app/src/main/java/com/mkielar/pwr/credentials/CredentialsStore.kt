package com.mkielar.pwr.credentials

interface CredentialsStore {
    fun putCredentials(login: String, password: String)
    fun getCredentials(): Pair<String?, String?>
    fun putTokens(jsessionid: String, appToken: String)
    fun getJsessionid(): String?
    fun getAppToken(): String?
}