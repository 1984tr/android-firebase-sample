package com.tr1984.firebasesample.ui.feeds

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tr1984.firebasesample.databinding.ActivityFeedWriteBinding

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
}