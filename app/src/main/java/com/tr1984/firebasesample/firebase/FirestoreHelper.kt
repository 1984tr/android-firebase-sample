package com.tr1984.firebasesample.firebase

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.tr1984.firebasesample.data.Poi

class FirestoreHelper private constructor() {

    private val fireestore by lazy {
        FirebaseFirestore.getInstance()
    }

    // maps/0/pois/0
    fun getMaps() {
        fireestore.collection("maps")
            .get()
            .addOnSuccessListener { maps ->
                maps.documents.forEach {
                    getPois(it)
                }
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
    }

    fun getPois(snapshot: DocumentSnapshot) {
        snapshot.reference.collection("pois")
            .get()
            .addOnSuccessListener {
                it.documents.forEach {
                    val poi = it.toObject(Poi::class.java)
                }
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
    }

    companion object {
        val instance = FirestoreHelper()
    }
}