package com.karimfikani.weatherapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // TODO: Initialize libraries that need to be initialized at the start of the app
    }
}
