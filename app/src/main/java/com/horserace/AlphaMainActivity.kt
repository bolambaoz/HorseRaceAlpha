package com.horserace

import android.app.Application
import com.horserace.data.db.AppDatabase
import com.horserace.data.network.MyApi
import com.horserace.data.network.NetworkConnectionInterceptor
import com.horserace.data.repository.HorseRaceRepository
import com.horserace.data.repository.UserRepository
import com.horserace.ui.channels.GalleryViewFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class AlphaMainActivity : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@AlphaMainActivity))

        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { MyApi( instance()) }
        bind() from singleton { AppDatabase( instance()) }
        bind() from singleton { UserRepository( instance(), instance()) }
        bind() from singleton { HorseRaceRepository( instance(), instance()) }
        bind() from provider { GalleryViewFactory( instance()) }
//        bind() from provider { AuthViewModelFactory( instance()) }
    }
}