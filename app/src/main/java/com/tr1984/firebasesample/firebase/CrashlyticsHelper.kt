package com.tr1984.firebasesample.firebase

import com.crashlytics.android.Crashlytics

object CrashlyticsHelper {

    fun setUserProperties() {
        Crashlytics.setUserName("1984tr")
        Crashlytics.setUserEmail("1984tr@1984tr.com")
        Crashlytics.setUserIdentifier("id_1984tr")
    }

    fun log(message: String) {
        Crashlytics.log(message)
    }

    fun crash() {
        Crashlytics.getInstance().crash()
    }
}