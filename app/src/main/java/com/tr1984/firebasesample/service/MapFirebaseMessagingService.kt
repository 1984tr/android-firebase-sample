package com.tr1984.firebasesample.service

import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService

class MapFirebaseMessagingService : FirebaseMessagingService() {



    fun test() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener {
                
            }
    }
}