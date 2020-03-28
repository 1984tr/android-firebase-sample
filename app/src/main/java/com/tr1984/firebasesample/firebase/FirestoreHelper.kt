package com.tr1984.firebasesample.firebase

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.tr1984.firebasesample.data.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class FirestoreHelper private constructor() {

    private val firestore by lazy {
        FirebaseFirestore.getInstance()
    }

    // maps/0/pois/0
    fun getPois(): Single<List<Pois>> {
        return getMap()
            .flatMapObservable { Observable.fromIterable(it.documents) }
            .flatMapSingle { getPois(it) }
            .toList()
    }

    private fun getMap(): Single<QuerySnapshot> {
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

    private fun getPois(snapshot: DocumentSnapshot): Single<Pois> {
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

    fun getFeeds(): Single<List<Feed>> {
        return Single.create { emit ->
            firestore.collection("feeds")
                .get()
                .addOnSuccessListener { result ->
                    emit.onSuccess(result.documents.map {
                        (it.toObject(Feed::class.java) ?: Feed()).apply {
                            id = it.reference.id
                        }
                    })
                }
                .addOnFailureListener {
                    it.printStackTrace()
                    emit.onError(it)
                }
        }
    }

    fun insertFeed(feed: Feed): Completable {
        return Completable.fromCallable {
            firestore.collection("feeds")
                .document()
                .set(feed)
                .addOnCompleteListener {
                    it.isSuccessful
                }
        }
    }

    fun deleteFeed(documentPath: String): Completable {
        return Completable.fromCallable {
            firestore.collection("feeds")
                .document(documentPath)
                .delete()
                .addOnCompleteListener {
                    it.isSuccessful
                }
        }
    }

    fun insertReply(feedPath: String, reply: Reply): Completable {
        return Completable.fromCallable {
            firestore.collection("feeds")
                .document(feedPath)
                .collection("replies")
                .add(reply)
                .addOnCompleteListener {
                    it.isSuccessful
                }
        }
    }

    fun deleteReply(feedPath: String, replyPath: String): Completable {
        return Completable.fromCallable {
            firestore.collection("feeds")
                .document(feedPath)
                .collection("replies")
                .document(replyPath)
                .delete()
                .addOnCompleteListener {
                    it.isSuccessful
                }
        }
    }

    fun insertReReply(feedPath: String, replayPath: String, rereply: ReReply): Completable {
        return Completable.fromCallable {
            firestore.collection("feeds")
                .document(feedPath)
                .collection("replies")
                .document(replayPath)
                .collection("rereplies")
                .add(rereply)
                .addOnCompleteListener {
                    it.isSuccessful
                }
        }
    }

    fun deleteReReply(feedPath: String, replyPath: String, rereplyPath: String): Completable {
        return Completable.fromCallable {
            firestore.collection("feeds")
                .document(feedPath)
                .collection("replies")
                .document(replyPath)
                .collection("rereplies")
                .document(rereplyPath)
                .delete()
                .addOnCompleteListener {
                    it.isSuccessful
                }
        }
    }

    fun getRepliess(documentPath: String): Single<List<DocumentSnapshot>> {
        return Single.create { emit ->
            firestore.collection("feeds")
                .document(documentPath)
                .collection("replies")
                .get()
                .addOnSuccessListener { result ->
                    emit.onSuccess(result.documents)
                }
                .addOnFailureListener {
                    it.printStackTrace()
                    emit.onError(it)
                }
        }
    }

    fun getReplies(documentPath: String) : Single<List<Reply>> {
        return getRepliess(documentPath)
            .flatMapObservable { Observable.fromIterable(it) }
            .flatMapSingle { getReply(it) }
            .toList()
    }

    fun getReply(snapshot: DocumentSnapshot): Single<Reply> {
        return Single.create { emit ->
            val reply = (snapshot.toObject(Reply::class.java) ?: Reply()).apply {
                id = snapshot.reference.id
                replies = arrayListOf()
            }
            snapshot.reference.collection("child")
                .get()
                .addOnSuccessListener { result ->
                    reply.replies.addAll(result.documents.map {
                        (it.toObject(ReReply::class.java) ?: ReReply()).apply {
                            id = it.reference.id
                        }
                    })
                    emit.onSuccess(reply)
                }
                .addOnFailureListener {
                    it.printStackTrace()
                    emit.onError(it)
                }
        }
    }

    companion object {
        val instance = FirestoreHelper()
    }
}