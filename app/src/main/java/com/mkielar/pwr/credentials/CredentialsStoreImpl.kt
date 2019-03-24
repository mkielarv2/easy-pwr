package com.mkielar.pwr.credentials

import `in`.co.ophio.secure.core.KeyStoreKeyGenerator
import `in`.co.ophio.secure.core.ObscuredPreferencesBuilder
import android.app.Application
import android.content.SharedPreferences

class CredentialsStoreImpl(application: Application) : CredentialsStore {
    private val sharedPreferences: SharedPreferences

    init {
        sharedPreferences = getSharedPreferences(application)
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

    override fun putCredentials(login: String, password: String) {
        sharedPreferences.edit()
            .putString(LOGIN_KEY, login)
            .putString(PASSWORD_KEY, password)
            .apply()
    }

    override fun getCredentials(): Pair<String?, String?> {
        val login = sharedPreferences.getString(LOGIN_KEY, null)
        val password = sharedPreferences.getString(PASSWORD_KEY, null)
        return login to password
    }

    override fun putTokens(jsessionid: String, appToken: String) {
        sharedPreferences.edit()
            .putString(JSESSIONID_KEY, jsessionid)
            .putString(APPTOKEN_KEY, appToken)
            .apply()
    }

    override fun getJsessionid(): String? = sharedPreferences.getString(JSESSIONID_KEY, null)

    override fun getAppToken(): String? = sharedPreferences.getString(APPTOKEN_KEY, null)

    override fun putJsosCredentials(login: String, password: String) {
        sharedPreferences.edit()
            .putString(JSOS_LOGIN_KEY, login)
            .putString(JSOS_PASSWORD_KEY, password)
            .apply()
    }

    override fun getJsosCredentials(): Pair<String?, String?> {
        val login = sharedPreferences.getString(JSOS_LOGIN_KEY, null)
        val password = sharedPreferences.getString(JSOS_PASSWORD_KEY, null)
        return login to password
    }

    override fun putJsosSessionIdCookie(jsosSessionId: String) {
        sharedPreferences.edit()
            .putString(JSOS_SESSION_ID_KEY, jsosSessionId)
            .apply()
    }

    override fun getJsosSessionIdCookie(): String? =
        sharedPreferences.getString(JSOS_SESSION_ID_KEY, null)


    companion object {
        private const val LOGIN_KEY: String = "login"
        private const val PASSWORD_KEY: String = "password"
        private const val JSESSIONID_KEY: String = "jsessionid"
        private const val APPTOKEN_KEY: String = "apptoken"

        private const val JSOS_LOGIN_KEY: String = "jsos_login"
        private const val JSOS_PASSWORD_KEY: String = "jsos_password"
        private const val JSOS_SESSION_ID_KEY: String = "jsos_session_id"
    }
}
