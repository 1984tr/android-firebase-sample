package com.tr1984.firebasesample.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthenticationHelper private constructor() {

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    fun getUid() = firebaseAuth.uid ?: "unknown"

    fun isSigned() = firebaseAuth.currentUser != null

    fun signInAnonymously(completion: (FirebaseUser?) -> Unit) {
        firebaseAuth.signInAnonymously()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    completion.invoke(firebaseAuth.currentUser)
                } else {
                    completion.invoke(null)
                }
            }
    }

    companion object {

        val instance = AuthenticationHelper()
    }
}