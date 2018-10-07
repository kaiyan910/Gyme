package com.architecture.kotlinmvvm.base

import android.app.Application
import com.architecture.kotlinmvvm.preferences.ConcealPreferences

open class BaseApp : Application() {

    override fun onCreate() {
        super.onCreate()
        ConcealPreferences.init(this)
    }
}