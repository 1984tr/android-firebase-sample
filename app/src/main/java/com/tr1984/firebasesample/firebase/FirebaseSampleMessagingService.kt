package com.tr1984.firebasesample.firebase

import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.tr1984.firebasesample.util.Logger
import com.tr1984.firebasesample.util.Preferences

class FirebaseSampleMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Logger.d("onNewToken - token: $token")
        Preferences.put(Preferences.Key.FcmToken, token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Logger.d("onMessageReceived - $remoteMessage")
        remoteMessage.notification?.run {

        }
    }

    companion object {

        fun getInstanceId(completion: (String?) -> Unit) {
            FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val token = it.result?.token
                        Logger.d("getInstanceId success: $token")
                        completion.invoke(token)
                    } else {
                        Logger.w("getInstanceId failed: ${it.exception}")
                        completion.invoke(null)
                    }
                }
        }
    }
}