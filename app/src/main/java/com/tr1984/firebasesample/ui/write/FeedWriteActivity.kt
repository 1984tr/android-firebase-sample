package com.tr1984.firebasesample.ui.write

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.tr1984.firebasesample.databinding.ActivityFeedWriteBinding
import com.tr1984.firebasesample.extensions.disposeBag
import com.tr1984.firebasesample.extensions.toast
import com.tr1984.firebasesample.extensions.uiSubscribeWithError
import com.tr1984.firebasesample.firebase.FireStorageHelper

class FeedWriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeedWriteBinding
    private lateinit var viewModel: FeedWriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = FeedWriteViewModel()
        binding = ActivityFeedWriteBinding.inflate(layoutInflater)
        binding.activity = this
        binding.viewModel = viewModel
        setContentView(binding.root)

        subscribeViewModel()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 9001 && resultCode == Activity.RESULT_OK) {
            try {
                data?.let {
                    val inputStream = contentResolver.openInputStream(it.data)
                    val img = BitmapFactory.decodeStream(inputStream)
                    inputStream.close()
                    binding.attachmentImage.setImageBitmap(img)
                    val path = getRealPathFromURI(it.data)
                    Log.d("test", "path: $path")
                    path?.run {
                        FireStorageHelper.upload(this)
                            .uiSubscribeWithError {
                                Log.d("test", "complete: $it")
                            }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.destroy()
    }

    fun moveGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        startActivityForResult(intent, 9001)
    }

    private fun getRealPathFromURI(contentUri: Uri): String? {
        if (contentUri.path.startsWith("/storage")) {
            return contentUri.path
        }
        val id = DocumentsContract.getDocumentId(contentUri).split(":")[1]
        val columns = arrayOf(MediaStore.Files.FileColumns.DATA)
        val selection = MediaStore.Files.FileColumns._ID + " = " + id
        val cursor = contentResolver.query(
            MediaStore.Files.getContentUri("external"),
            columns,
            selection,
            null,
            null
        )
        try {
            val columnIndex = cursor.getColumnIndex(columns[0])
            if (cursor.moveToFirst()) {
                return cursor.getString(columnIndex)
            }
        } finally {
            cursor.close()
        }
        return null
    }

    private fun subscribeViewModel() {
        viewModel.run {
            toastSubject.uiSubscribeWithError {
                this@FeedWriteActivity.toast(it)
            }.disposeBag(compositeDisposable)

            uploadCompleteSubject.uiSubscribeWithError {
                setResult(Activity.RESULT_OK)
                finish()
            }.disposeBag(compositeDisposable)
        }
    }
}