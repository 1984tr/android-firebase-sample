package com.tr1984.firebasesample.firebase

import com.crashlytics.android.Crashlytics

object CrashlyticsHelper {

    fun setUserProperties(uid: String) {
        Crashlytics.setUserName("name: $uid")
        Crashlytics.setUserEmail("$uid@sample.com")
        Crashlytics.setUserIdentifier(uid)
    }

    fun log(message: String) {
        Crashlytics.log(message)
    }

    fun crash() {
        Crashlytics.getInstance().crash()
    }
}