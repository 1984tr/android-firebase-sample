package com.tr1984.firebasesample.ui.feeds

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tr1984.firebasesample.databinding.ActivityFeedWriteBinding

class FeedWriteActivity: AppCompatActivity() {

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

    override fun onDestroy() {
        super.onDestroy()
        viewModel.destroy()
    }
}