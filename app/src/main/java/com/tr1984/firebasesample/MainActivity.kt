package com.tr1984.firebasesample

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.tr1984.firebasesample.firebase.AnalyticsHelper
import com.tr1984.firebasesample.firebase.CrashlyticsHelper

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AnalyticsHelper.instance.trackScreen(this)
    }

    fun onClickAnalytics(v: View) {
        AnalyticsHelper.instance.logEvent(AnalyticsHelper.Event.CLICK_BTN_ANALYTICS)
    }

    fun onClickCrashlytics(v: View) {
        CrashlyticsHelper.crash()
    }
}
