package com.horseracingtips

import android.app.Application
import com.horseracingtips.data.db.AppDatabase
import com.horseracingtips.data.db.preferrences.PreferenceProvider
import com.horseracingtips.data.network.MyApi
import com.horseracingtips.data.network.NetworkConnectionInterceptor
import com.horseracingtips.data.repository.HorseRaceRepository
import com.horseracingtips.data.repository.UserRepository
import com.horseracingtips.ui.channels.GalleryViewFactory
import com.horseracingtips.ui.dashboard.HomeViewFactory
import com.horseracingtips.ui.videostream.VideoSteamViewModel
import com.horseracingtips.ui.videostream.VideoStreamFactory
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
        bind() from singleton { PreferenceProvider(instance()) }
        bind() from singleton { UserRepository( instance(), instance()) }
        bind() from singleton { HorseRaceRepository( instance(), instance(), instance()) }
//        bind() from provider { MainFactory( instance()) }
        bind() from provider { GalleryViewFactory( instance()) }
        bind() from provider { HomeViewFactory( instance()) }
        bind() from provider { VideoStreamFactory( instance()) }
//        bind() from provider { AuthViewModelFactory( instance()) }
    }
}