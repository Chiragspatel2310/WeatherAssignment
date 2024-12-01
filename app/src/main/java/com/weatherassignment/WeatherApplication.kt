package com.weatherassignment

import android.app.Application
import com.shashank.weatherapp.data.network.NetworkState
import com.weatherassignment.data.local.WeatherDatabase
import com.weatherassignment.data.remote.RetrofitService
import com.weatherassignment.data.repositories.WeatherRepository
import com.weatherassignment.ui.viewmodelfactory.WeatherViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class WeatherApplication : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@WeatherApplication))
        bind() from singleton { NetworkState(instance()) }
        bind() from singleton { RetrofitService(instance()) }
        bind() from singleton { WeatherRepository(instance(), instance()) }
        bind() from provider { WeatherViewModelFactory(instance()) }
        bind() from provider { WeatherDatabase(instance()) }
    }


}
