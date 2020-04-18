package com.tr1984.firebasesample

import android.app.Application
import com.tr1984.firebasesample.firebase.AnalyticsHelper
import com.tr1984.firebasesample.util.Preferences

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Preferences.init(this)

        AnalyticsHelper.instance.run {
            setup(this@MainApplication)
            logEvent("start_application")
        }
    }
}