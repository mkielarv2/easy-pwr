package com.mkielar.pwr.jsos.api.network

import androidx.room.Entity

@Entity(primaryKeys = [("url")])
data class JsosEmail(
    val timestamp: Long,
    val url: String,
    val sender: String,
    val title: String
)