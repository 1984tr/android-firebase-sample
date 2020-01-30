package com.tr1984.firebasesample

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tr1984.firebasesample.firebase.AnalyticsHelper
import com.tr1984.firebasesample.firebase.CrashlyticsHelper
import com.tr1984.firebasesample.firebase.DynamicLinkHelper
import com.tr1984.firebasesample.firebase.RemoteConfigHelper

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

    fun onClickDynamicLink(v: View) {
        DynamicLinkHelper.getShortDynamicLink("https://github.com/1984tr/android-firebase-sample") {
            it?.let { uri ->
                val intent = Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_TEXT, uri.toString())
                }
                startActivity(Intent.createChooser(intent, "Share"))
            } ?: Toast.makeText(this@MainActivity, "Retry later :(", Toast.LENGTH_SHORT).show()
        }
    }

    fun onClickRemoteConfig(v: View) {
        RemoteConfigHelper.instance.fetchAndActivate {
            if (it) {
                val minVersion =
                    RemoteConfigHelper.instance.getString(RemoteConfigHelper.Key.MINIMUM_VERSION)
                Log.d("fbSample", "minVersion: $minVersion")
            } else {
                Toast.makeText(this@MainActivity, "Retry later :(", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun onClickAuthentification(v: View) {
        
    }
}
