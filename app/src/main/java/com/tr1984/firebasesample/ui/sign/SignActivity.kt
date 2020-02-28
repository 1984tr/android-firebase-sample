package com.tr1984.firebasesample.ui.sign

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.tr1984.firebasesample.R
import com.tr1984.firebasesample.firebase.AnalyticsHelper
import com.tr1984.firebasesample.firebase.AuthenticationHelper
import com.tr1984.firebasesample.firebase.CrashlyticsHelper
import com.tr1984.firebasesample.ui.main.MapsActivity
import com.tr1984.firebasesample.util.Logger
import com.tr1984.firebasesample.util.Preferences

class SignActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)

        if (AuthenticationHelper.instance.isSigned()) {
            initialize()
            startMap()
        }
    }

    fun start(view: View) {
        AuthenticationHelper.instance.signInAnonymously { user ->
            user?.getIdToken(false)?.addOnCompleteListener {
                Preferences.put(Preferences.Key.CreatedAt, System.currentTimeMillis())

                initialize()
                startMap()
            }
        }
    }

    private fun initialize() {
        val uid = AuthenticationHelper.instance.getUid()
        Logger.d("SignActivity.initialize uid: $uid")

        AnalyticsHelper.instance.setUserProperties(uid, Preferences.getLong(Preferences.Key.CreatedAt) ?: 0L)
        CrashlyticsHelper.setUserProperties(uid)
    }

    private fun startMap() {
        startActivity(Intent(this, MapsActivity::class.java))
        finish()
    }
}