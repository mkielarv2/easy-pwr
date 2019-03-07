package com.mkielar.pwr.email.model

data class EmailDetails(
    val timestamp: String,
    val subject: String,
    val sender: String,
    val content: String
)