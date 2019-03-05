package com.mkielar.pwr

import `in`.co.ophio.secure.core.KeyStoreKeyGenerator
import `in`.co.ophio.secure.core.ObscuredPreferencesBuilder
import android.app.Application
import android.content.SharedPreferences

class CredentialsStore(application: Application) {
    companion object {
        private const val LOGIN_KEY: String = "login"
        private const val PASSWORD_KEY: String = "password"
        private const val JSESSIONID_KEY: String = "jsessionid"
        private const val APPTOKEN_KEY: String = "apptoken"
    }

    private val sharedPreferences: SharedPreferences

    init {
        sharedPreferences = getSharedPreferences(application)
    }

    fun putCredentials(login: String, password: String) {
        sharedPreferences.edit()
            .putString(LOGIN_KEY, login)
            .putString(PASSWORD_KEY, password)
            .apply()
    }

    fun getCredentials(): Pair<String?, String?> {
        val login = sharedPreferences.getString(LOGIN_KEY, null)
        val password = sharedPreferences.getString(PASSWORD_KEY, null)
        return login to password
    }

    private fun getSharedPreferences(application: Application): SharedPreferences = ObscuredPreferencesBuilder()
        .setApplication(application)
        .obfuscateKey(true)
        .obfuscateValue(true)
        .setSharePrefFileName("${application.packageName}.credentials")
        .setSecret(
            KeyStoreKeyGenerator.get(application, application.baseContext.packageName)
                .loadOrGenerateKeys()
        )
        .createSharedPrefs()

    fun putTokens(jsessionid: String, appToken: String) {
        sharedPreferences.edit()
            .putString(JSESSIONID_KEY, jsessionid)
            .putString(APPTOKEN_KEY, appToken)
            .apply()
    }

    fun getJsessionid() = sharedPreferences.getString(JSESSIONID_KEY, null)

    fun getAppToken() = sharedPreferences.getString(APPTOKEN_KEY, null)

}