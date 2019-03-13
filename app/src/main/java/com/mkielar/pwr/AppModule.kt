package com.mkielar.pwr

import androidx.room.Room
import com.mkielar.pwr.credentials.CredentialsStore
import com.mkielar.pwr.credentials.CredentialsStoreImpl
import com.mkielar.pwr.database.AppDatabase
import com.mkielar.pwr.email.viewModel.EmailViewModel
import com.mkielar.pwr.email.viewModel.network.*
import com.mkielar.pwr.email.viewModel.parse.EmailDetailsParser
import com.mkielar.pwr.email.viewModel.parse.EmailDetailsParserImpl
import com.mkielar.pwr.email.viewModel.parse.EmailParser
import com.mkielar.pwr.email.viewModel.parse.EmailParserImpl
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

object AppModule {
    val appModule = module {
        single {
            Room.databaseBuilder<AppDatabase>(
                get(),
                AppDatabase::class.java,
                "AppDatabase.db"
            ).build()
        }

        viewModel { EmailViewModel(get(), get(), get()) }

        single { CredentialsStoreImpl(get()) as CredentialsStore }

        single { EmailAuthenticatorImpl(get()) as EmailAuthenticator }

        single { EmailDetailsParserImpl() as EmailDetailsParser }
        single { EmailDetailsDownloaderImpl(get(), get()) as EmailDetailsDownloader }

        single { EmailDownloaderImpl(get(), get(), get()) as EmailDownloader }
        single { EmailParserImpl() as EmailParser }
    }
}