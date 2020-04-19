package com.tr1984.firebasesample.ui.write

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.tr1984.firebasesample.databinding.ActivityFeedWriteBinding
import com.tr1984.firebasesample.extensions.disposeBag
import com.tr1984.firebasesample.extensions.toast
import com.tr1984.firebasesample.extensions.uiSubscribeWithError
import com.tr1984.firebasesample.firebase.AnalyticsHelper
import io.reactivex.disposables.CompositeDisposable

class FeedWriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeedWriteBinding
    private lateinit var viewModel: FeedWriteViewModel
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AnalyticsHelper.instance.trackScreen(this)

        viewModel = FeedWriteViewModel(compositeDisposable)
        binding = ActivityFeedWriteBinding.inflate(layoutInflater)
        binding.activity = this
        binding.viewModel = viewModel
        setContentView(binding.root)

        subscribeViewModel()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RequestCodeToGallery && resultCode == Activity.RESULT_OK) {
            try {
                data?.data?.let {
                    val inputStream = contentResolver.openInputStream(it)
                    val img = BitmapFactory.decodeStream(inputStream)
                    inputStream?.close()
                    binding.attachmentImage.setImageBitmap(img)
                    binding.attachmentImage.visibility = View.VISIBLE
                    val path = getRealPathFromURI(it)
                    path?.let { p ->
                        viewModel.path = p
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                toast("잠시 후 다시 시도해주세요 :(")
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            RequestCodeToExternalStoragePermission -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                        type = "image/*"
                    }
                    startActivityForResult(intent, RequestCodeToGallery)
                } else {
                    toast("저장 권한이 필요합니다.")
                }
                return
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun moveGallery() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                toast("저장 권한이 필요합니다.")
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), RequestCodeToExternalStoragePermission)
            }
            return
        }

        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        startActivityForResult(intent, RequestCodeToGallery)
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

            progressSubject.uiSubscribeWithError {
                binding.viewProgress.visibility = if (it) View.VISIBLE else View.GONE
            }.disposeBag(compositeDisposable)
        }
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

    companion object {
        const val RequestCodeToGallery = 9001
        const val RequestCodeToExternalStoragePermission = 9002
    }
}