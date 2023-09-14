package com.example.study

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        app = this
    }

    companion object {
        var app: App? = null
    }
}