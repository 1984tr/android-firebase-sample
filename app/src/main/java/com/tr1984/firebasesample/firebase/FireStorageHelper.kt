package com.tr1984.firebasesample.firebase

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import io.reactivex.Single
import java.io.File

object FireStorageHelper {

    private val storge by lazy {
        FirebaseStorage.getInstance("gs://fir-sample-aaf9e.appspot.com/")
    }

    fun upload(path: String): Single<String> {
        return Single.create<String> { emit ->
            val ref = storge.reference
            val f = File(path)
            val file = Uri.fromFile(f)

            val metadata = StorageMetadata.Builder()
                .setContentType("image/${f.extension}")
                .build()

            val storageRef = ref.child("feeds/${file.lastPathSegment}")
            val uploadTask = storageRef.putFile(file, metadata)

            // Listen for state changes, errors, and completion of the upload.
            uploadTask.addOnProgressListener { taskSnapshot ->
                val progress = (100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount
                Log.d("Test", "Upload is $progress% done")
            }.addOnPausedListener {
                Log.d("Test", "Upload is paused")
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
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
                } else {
                    Log.d("Test", "error: ${task.exception?.message}")
                    task.exception?.let { error ->
                        emit.onError(error)
                    } ?: emit.onError(Exception("Failed to put file."))
                }
            }
        }
    }
}