package com.tr1984.firebasesample.firebase

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import io.reactivex.Single
import java.io.File

class FireStorageHelper private constructor() {

    private val storge by lazy {
        FirebaseStorage.getInstance("gs://fir-sample-aaf9e.appspot.com/")
    }

    fun upload(path: String) : Single<String> {
        return Single.create<String> { emit ->
            val ref = storge.reference
            val file = Uri.fromFile(File("path/to/mountains.jpg"))

            val metadata = StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .build()

            val uploadTask = ref.child("feeds/${file.lastPathSegment}").putFile(file, metadata)

            // Listen for state changes, errors, and completion of the upload.
            uploadTask.addOnProgressListener { taskSnapshot ->
                val progress = (100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount
                Log.d("Test", "Upload is $progress% done")
            }.addOnPausedListener {
                Log.d("Test", "Upload is paused")
            }.addOnFailureListener {
                // Handle unsuccessful uploads
            }.addOnSuccessListener { task ->
                // Handle successful uploads on complete
                // ...
            }.addOnCompleteListener {
                if (it.isSuccessful) {
                    val downloadUri = it.result
                    Log.d("Test", "downloadUri: $downloadUri")
                } else {
                    Log.d("Test", "error: ${it.exception?.message}")
                }
            }
        }
    }
}