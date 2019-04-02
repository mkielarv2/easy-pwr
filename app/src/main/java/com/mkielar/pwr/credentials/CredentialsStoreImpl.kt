package com.mkielar.pwr.credentials

import `in`.co.ophio.secure.core.KeyStoreKeyGenerator
import `in`.co.ophio.secure.core.ObscuredPreferencesBuilder
import android.app.Application
import android.content.SharedPreferences

class CredentialsStoreImpl(application: Application) : CredentialsStore {
    private val sharedPreferences: SharedPreferences = ObscuredPreferencesBuilder()
        .setApplication(application)
        .obfuscateKey(true)
        .obfuscateValue(true)
        .setSharePrefFileName("${application.packageName}.credentials")
        .setSecret(
            KeyStoreKeyGenerator.get(application, application.baseContext.packageName)
                .loadOrGenerateKeys()
        )
        .createSharedPrefs()

    override fun putValue(key: String, value: String) {
        sharedPreferences.edit()
            .putString(key, value)
            .apply()
    }

    override fun getValue(key: String) = sharedPreferences.getString(key, null) ?: throw MissingValueException()

    override fun hasValue(key: String) = sharedPreferences.contains(key)
}
