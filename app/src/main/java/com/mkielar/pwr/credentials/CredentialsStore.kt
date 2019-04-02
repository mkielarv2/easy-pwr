package com.mkielar.pwr.credentials

interface CredentialsStore {
    fun putValue(key: String, value: String)
    fun getValue(key: String): String
    fun hasValue(key: String): Boolean
}