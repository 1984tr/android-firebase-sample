package com.tr1984.firebasesample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tr1984.firebasesample.firebase.AnalyticsHelper

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AnalyticsHelper.instance.trackScreen(this)
    }
}
