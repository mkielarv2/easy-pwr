package com.mkielar.pwr.email.inbox.model

import androidx.room.Entity

@Entity(primaryKeys = [("id")])
data class Email(
    val id: Int,
    val size: Long,
    val timestamp: Long,
    val sender: String,
    val title: String
)