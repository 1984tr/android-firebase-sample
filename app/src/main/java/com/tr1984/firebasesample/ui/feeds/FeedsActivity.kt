package com.tr1984.firebasesample.ui.feeds

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.tr1984.firebasesample.databinding.ActivityFeedsBinding

class FeedsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeedsBinding
    private lateinit var viewModel: FeedsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = FeedsViewModel()
        binding = ActivityFeedsBinding.inflate(layoutInflater)
        binding.activity = this
        binding.viewModel = viewModel
        setContentView(binding.root)

        setupUI()

        viewModel.start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            9111 -> {
                if (resultCode == Activity.RESULT_OK) {
                    viewModel.refresh()
                }
            }
            else -> {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    fun moveToWrite() {
        startActivityForResult(Intent(this, FeedWriteActivity::class.java), 9111)
    }

    private fun setupUI() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = FeedsAdapter(viewModel.items)
    }
}