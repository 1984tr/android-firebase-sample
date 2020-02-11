package com.tr1984.firebasesample.firebase

import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.tr1984.firebasesample.util.Logger
import com.tr1984.firebasesample.util.Preferences

class MapFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Logger.d("onNewToken - token: $token")
        Preferences.put(Preferences.Key.FcmToken, token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Logger.d("onMessageReceived - ${remoteMessage.toString()}")
        remoteMessage.notification?.run {

        }
    }

    fun test() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener {
                
            }
    }
}