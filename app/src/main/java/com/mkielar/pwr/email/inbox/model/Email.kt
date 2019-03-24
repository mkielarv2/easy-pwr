package com.mkielar.pwr.email.inbox.model

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(primaryKeys = [("id")])
data class Email(
    val id: Int,
    val size: Long,
    val jsosStatus: Int,
    val timestamp: Long,
    val sender: String,
    val title: String
) : Parcelable