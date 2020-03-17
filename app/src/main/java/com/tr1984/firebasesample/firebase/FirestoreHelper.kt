package com.tr1984.firebasesample.firebase

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.tr1984.firebasesample.data.Poi
import com.tr1984.firebasesample.data.Pois
import io.reactivex.Observable
import io.reactivex.Single

class FirestoreHelper private constructor() {

    private val firestore by lazy {
        FirebaseFirestore.getInstance()
    }

    // maps/0/pois/0
    fun getPois() : Single<List<Pois>> {
        return getMap()
            .flatMapObservable { Observable.fromIterable(it.documents) }
            .flatMapSingle { getPois(it) }
            .toList()
    }

    private fun getMap() : Single<QuerySnapshot> {
        return Single.create { emit ->
            firestore.collection("maps")
                .get()
                .addOnSuccessListener {
                    emit.onSuccess(it)
                }
                .addOnFailureListener {
                    it.printStackTrace()
                    emit.onError(it)
                }
        }
    }

    private fun getPois(snapshot: DocumentSnapshot) : Single<Pois> {
        return Single.create { emit ->
            val pois = snapshot.toObject(Pois::class.java) ?: Pois()
            snapshot.reference.collection("pois")
                .get()
                .addOnSuccessListener { result ->
                    pois.items.addAll(result.documents.map {
                        it.toObject(Poi::class.java) ?: Poi()
                    })
                    emit.onSuccess(pois)
                }
                .addOnFailureListener {
                    it.printStackTrace()
                    emit.onError(it)
                }
        }
    }

 

//    fun getFeeds(callback: (List<Feed>) -> Unit) {
//        firestore.collection("feeds").orderBy("time", Query.Direction.DESCENDING)
//            .get().addOnSuccessListener { listener ->
//                callback(listener.result.toObjects(Feed::class.java))
//            }
//    }
//
//    private fun getFeeds() : Single<QuerySnapshot> {
//        return Single.create { emit ->
//            firestore.collection("feeds")
//                .get()
//                .addOnSuccessListener {
//                    it.documents.map {
//                        it.toObject(Feed::class.java) ?: Feed()
//                    }
//                    emit.onSuccess(it)
//                }
//                .addOnFailureListener {
//                    it.printStackTrace()
//                    emit.onError(it)
//                }
//        }
//    }
//
//    fun insertFeed(feed: Feed, callBack: (isSucess: Boolean) -> Unit) {
//        val target = if (feed.id <= 0) {
//            firestore.collection("feeds").document()
//        } else {
//            firestore.collection("feeds").document(feed.id.toString())
//        }
//
//        feed.id = target.id
//
//        targetPost.set(post).addOnCompleteListener { result ->
//            callBack(result.isSuccessful)
//        }
//    }
//
//    fun deleteFeed(id: String, completion: (success: Boolean) -> Unit) {
//        firestore.collection("feeds").document(id).delete()
//            .addOnCompleteListener { listener -> completion.invoke(listener.isSuccessful) }
//            .addOnFailureListener { completion.invoke(false) }
//    }

    companion object {
        val instance = FirestoreHelper()
    }
}