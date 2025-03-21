package com.akilliMutfak

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class AkilliMutfakApp : Application() {
    
    companion object {
        val isDarkMode = mutableStateOf(false)
    }

    override fun onCreate() {
        super.onCreate()
        
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        // Firebase başlatma
        FirebaseApp.initializeApp(this)

        // WorkManager başlatma
        WorkManager.initialize(
            this,
            Configuration.Builder()
                .setMinimumLoggingLevel(if (BuildConfig.DEBUG) android.util.Log.DEBUG else android.util.Log.ERROR)
                .build()
        )
    }
}