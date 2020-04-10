package com.tr1984.firebasesample.ui.feeds

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.tr1984.firebasesample.databinding.ActivityFeedsBinding
import com.tr1984.firebasesample.extensions.disposeBag
import com.tr1984.firebasesample.extensions.toast
import com.tr1984.firebasesample.extensions.uiSubscribeWithError
import com.tr1984.firebasesample.ui.replies.RepliesActivity
import com.tr1984.firebasesample.ui.write.FeedWriteActivity

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
        subscribeViewModel()

        viewModel.start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            9111 -> {
                if (resultCode == Activity.RESULT_OK) {
                    viewModel.start()
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
        binding.recyclerView.run {
            layoutManager = LinearLayoutManager(this@FeedsActivity)
            adapter = FeedsAdapter(viewModel.items)
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.start()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun subscribeViewModel() {
        viewModel.run {
            toastSubject
                .uiSubscribeWithError {
                    toast(it)
                }.disposeBag(compositeDisposable)

            updateSubject
                .uiSubscribeWithError {
                    binding.recyclerView.adapter?.notifyDataSetChanged()
                }.disposeBag(compositeDisposable)

            showRepliesSubject
                .uiSubscribeWithError { feed ->
                    startActivity(Intent(this@FeedsActivity, RepliesActivity::class.java).apply {
                        putExtra("feedDocumentPath", feed.documentPath)
                    })
                }.disposeBag(compositeDisposable)
        }
    }
}