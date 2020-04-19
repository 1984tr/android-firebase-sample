package com.tr1984.firebasesample.firebase

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import io.reactivex.Single
import java.io.File

object FireStorageHelper {

    private val url = "gs://fir-sample-aaf9e.appspot.com/"
    private val storage by lazy {
        FirebaseStorage.getInstance(url)
    }

    fun upload(path: String): Single<String> {
        return putFile(path)
            .flatMap { getDownloadUrl(it) }
    }

    private fun putFile(path: String): Single<StorageReference> {
        return Single.create<StorageReference> { emit ->
            val ref = storage.reference
            val f = File(path)
            val file = Uri.fromFile(f)

            val metadata = StorageMetadata.Builder()
                .setContentType("image/${f.extension}")
                .build()

            val storageRef = ref.child("feeds/${file.lastPathSegment}")
            val uploadTask = storageRef.putFile(file, metadata)

            uploadTask.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    emit.onSuccess(storageRef)
                } else {
                    Log.d("Test", "error: ${task.exception?.message}")
                    task.exception?.let { error ->
                        emit.onError(error)
                    } ?: emit.onError(Exception("Failed to put file."))
                }
            }
        }
    }

    private fun getDownloadUrl(storageRef: StorageReference): Single<String> {
        return Single.create<String> { emit ->
            storageRef.downloadUrl
                .addOnCompleteListener { uri ->
                    if (uri.isSuccessful) {
                        emit.onSuccess(uri.result?.toString() ?: "")
                    } else {
                        uri.exception?.let { error ->
                            emit.onError(error)
                        } ?: emit.onError(Exception("Failed to get download uri."))
                    }
                }
        }
    }
}