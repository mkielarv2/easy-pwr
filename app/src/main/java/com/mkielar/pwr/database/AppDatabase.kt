package com.mkielar.pwr.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mkielar.pwr.email.inbox.model.Email
import com.mkielar.pwr.jsos.api.network.JsosEmail


@Database(entities = [Email::class, JsosEmail::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun emailDao(): EmailDao

    abstract fun jsosEmailDao(): JsosEmailDao

    companion object {
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder<AppDatabase>(
                    context,
                    AppDatabase::class.java, "AppDatabase.db"
                ).build()
            }
            return instance as AppDatabase
        }
    }
}