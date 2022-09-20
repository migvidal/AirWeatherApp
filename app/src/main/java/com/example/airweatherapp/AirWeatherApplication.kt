package com.example.airweatherapp

import android.app.Application
import timber.log.Timber

class AirWeatherApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}