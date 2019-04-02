package com.mkielar.pwr

import androidx.room.Room
import com.mkielar.pwr.credentials.CredentialsStore
import com.mkielar.pwr.credentials.CredentialsStoreImpl
import com.mkielar.pwr.database.AppDatabase
import com.mkielar.pwr.email.api.network.*
import com.mkielar.pwr.email.api.parse.EmailDetailsParser
import com.mkielar.pwr.email.api.parse.EmailDetailsParserImpl
import com.mkielar.pwr.email.api.parse.EmailParser
import com.mkielar.pwr.email.api.parse.EmailParserImpl
import com.mkielar.pwr.email.details.viewmodel.DetailsViewModel
import com.mkielar.pwr.email.inbox.viewmodel.EmailViewModel
import com.mkielar.pwr.jsos.api.network.JsosAuthenticator
import com.mkielar.pwr.jsos.api.network.JsosAuthenticatorImpl
import com.mkielar.pwr.jsos.api.network.JsosEmailDownloader
import com.mkielar.pwr.jsos.api.network.JsosEmailDownloaderImpl
import com.mkielar.pwr.main.viewmodel.MainViewModel
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
        viewModel { DetailsViewModel(get(), get()) }
        viewModel { MainViewModel(get(), get(), get()) }

        single { CredentialsStoreImpl(get()) as CredentialsStore }

        single { EmailAuthenticatorImpl(get()) as EmailAuthenticator }
        single { JsosAuthenticatorImpl(get()) as JsosAuthenticator }

        single { EmailDetailsParserImpl() as EmailDetailsParser }
        single {
            EmailDetailsDownloaderImpl(
                get(),
                get()
            ) as EmailDetailsDownloader
        }

        single { EmailParserImpl() as EmailParser }
        single {
            EmailDownloaderImpl(
                get(),
                get(),
                get()
            ) as EmailDownloader
        }

        single { JsosEmailDownloaderImpl(get(), get()) as JsosEmailDownloader }
    }
}