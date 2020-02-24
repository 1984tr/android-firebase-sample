package com.tr1984.firebasesample.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.tr1984.firebasesample.R
import com.tr1984.firebasesample.firebase.AuthenticationHelper

class SignActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)

        if (AuthenticationHelper.instance.isSigned()) {
            startMap()
        }
    }

    fun start(view: View) {
        AuthenticationHelper.instance.signInAnonymously { user ->
            user?.getIdToken(false)?.addOnCompleteListener {
                startMap()
            }
        }
    }

    private fun startMap() {
        startActivity(Intent(this, MapsActivity::class.java))
        finish()
    }
}