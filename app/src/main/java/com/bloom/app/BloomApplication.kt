package com.bloom.app

import android.app.Application
import com.bloom.app.data.local.PreferencesManager
import com.bloom.app.data.remote.ApiClient

class BloomApplication : Application() {

    companion object {
        lateinit var instance: BloomApplication
            private set
    }

    lateinit var preferencesManager: PreferencesManager
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        
        // Initialize preferences
        preferencesManager = PreferencesManager(this)
        
        // Initialize API client
        ApiClient.initialize(this)
    }
}