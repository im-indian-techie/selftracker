package com.ashin.selftracker

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.internal.Contexts.getApplication

@HiltAndroidApp
class MyApplication:Application() {
    override fun onCreate() {
        super.onCreate()
    }
}