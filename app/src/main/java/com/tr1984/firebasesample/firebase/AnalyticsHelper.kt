package com.tr1984.firebasesample.firebase

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

class AnalyticsHelper private constructor() {

    private lateinit var context: Context

    private val analytics by lazy {
        FirebaseAnalytics.getInstance(context)
    }

    fun setup(context: Context) {
        this.context = context
    }

    fun setUserProperties() {
        analytics.setUserId("id_1984tr")
        analytics.setUserProperty("name", "1984tr")
    }

    fun trackScreen(activity: Activity) {
        analytics.setCurrentScreen(activity, activity.javaClass.simpleName, null /* class override */)
    }

    fun logEvent(name: String, vararg params: Pair<String, Any>) {
        val bundle = Bundle().apply {
            params.forEach {
                when (it.second) {
                    is Boolean -> putBoolean(it.first, it.second as Boolean)
                    is Int -> putInt(it.first, it.second as Int)
                    is Float -> putFloat(it.first, it.second as Float)
                    is Long -> putLong(it.first, it.second as Long)
                    is Double -> putDouble(it.first, it.second as Double)
                    else -> putString(it.first, (it.second as? String) ?: "")
                }
            }
        }
        analytics.logEvent(name, bundle)
        CrashlyticsHelper.log(name)
    }

    object Event {
        val START_APPLICATION = "start_application"
        val CLICK_BTN_ANALYTICS = "click_btn_analytics"
    }

    object Param {
        val PARAM1 = "param1"
    }

    companion object {

        val instance = AnalyticsHelper()
    }
}