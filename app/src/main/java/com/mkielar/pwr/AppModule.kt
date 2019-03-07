package com.mkielar.pwr

import androidx.room.Room
import com.mkielar.pwr.credentials.CredentialsStore
import com.mkielar.pwr.credentials.CredentialsStoreImpl
import com.mkielar.pwr.database.AppDatabase
import com.mkielar.pwr.email.viewModel.*
import org.koin.dsl.module.module

abstract class AppModule {
    companion object {
        val appModule = module {
            single {
                Room.databaseBuilder<AppDatabase>(
                    get(),
                    AppDatabase::class.java,
                    "AppDatabase.db"
                ).build()
            }

            single { CredentialsStoreImpl(get()) as CredentialsStore }

            single { EmailAuthenticatorImpl(get()) as EmailAuthenticator }

            single { EmailDetailsParserImpl() as EmailDetailsParser }
            single { EmailDetailsDownloaderImpl(get(), get()) as EmailDetailsDownloader }

            single { EmailDownloaderImpl(get(), get(), get()) as EmailDownloader }
            single { EmailParserImpl() as EmailParser }
        }
    }
}