package com.tr1984.firebasesample.firebase

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.tr1984.firebasesample.data.Map
import com.tr1984.firebasesample.data.Poi

class FirestoreHelper private constructor() {

    private val firestore by lazy {
        FirebaseFirestore.getInstance()
    }

    private val map = Map()

    // maps/0/pois/0
    fun loadData(completion: (isSuccess: Boolean) -> Unit) {
        firestore.collection("maps")
            .get()
            .addOnSuccessListener { maps ->
                maps.documents.forEach {
                    getSubCollections(it, completion)
                }
            }
            .addOnFailureListener {
                it.printStackTrace()
                completion.invoke(false)
            }
    }

    private fun getSubCollections(snapshot: DocumentSnapshot, completion: (isSuccess: Boolean) -> Unit) {
        snapshot.reference.collection("pois")
            .get()
            .addOnSuccessListener {
                it.documents.mapIndexed { index, documentSnapshot ->
                    val poi = documentSnapshot.toObject(Poi::class.java)
                    if (index == it.documents.size) {
                        completion.invoke(true)
                    }
                }
            }
            .addOnFailureListener {
                it.printStackTrace()
                completion.invoke(false)
            }
    }

    companion object {
        val instance = FirestoreHelper()
    }
}