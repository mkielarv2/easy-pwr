package com.mkielar.pwr

import `in`.co.ophio.secure.core.KeyStoreKeyGenerator
import `in`.co.ophio.secure.core.ObscuredPreferencesBuilder
import android.app.Activity
import android.content.SharedPreferences

class CredentialsStore(activity: Activity) {
    companion object {
        private const val LOGIN_KEY: String = "login"
        private const val PASSWORD_KEY: String = "password"
    }

    private val sharedPreferences: SharedPreferences

    init {
        sharedPreferences = getSharedPreferences(activity)
    }

    fun putJsosCredentials(login: String, password: String) {
        sharedPreferences.edit()
            .putString(LOGIN_KEY, login)
            .putString(PASSWORD_KEY, password)
            .apply()
    }

    fun getJsosCredentials(): Pair<String?, String?> {
        val login = sharedPreferences.getString(LOGIN_KEY, null)
        val password = sharedPreferences.getString(PASSWORD_KEY, null)
        return login to password
    }

    private fun getSharedPreferences(activity: Activity): SharedPreferences = ObscuredPreferencesBuilder()
        .setApplication(activity.application)
        .obfuscateKey(true)
        .obfuscateValue(true)
        .setSharePrefFileName("${activity.applicationContext.packageName}.credentials")
        .setSecret(
            KeyStoreKeyGenerator.get(activity.application, activity.applicationContext.packageName)
                .loadOrGenerateKeys()
        )
        .createSharedPrefs()
}