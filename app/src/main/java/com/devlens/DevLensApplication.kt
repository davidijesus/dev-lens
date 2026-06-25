package com.devlens

import android.app.Application

class DevLensApplication : Application() {
    lateinit var container: DevLensContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = DevLensContainer(this)
    }
}
