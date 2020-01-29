package com.tr1984.firebasesample

import android.app.Application
import com.tr1984.firebasesample.firebase.AnalyticsHelper

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        AnalyticsHelper.instance.run {
            setup(this@MainApplication)
            setUserProperties()
            logEvent(AnalyticsHelper.Event.START_APPLICATION, AnalyticsHelper.Param.PARAM1 to "param1")
        }
    }
}