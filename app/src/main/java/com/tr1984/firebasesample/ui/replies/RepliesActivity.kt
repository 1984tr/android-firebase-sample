package com.tr1984.firebasesample.ui.replies

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.tr1984.firebasesample.databinding.ActivityRepliesBinding
import com.tr1984.firebasesample.extensions.disposeBag
import com.tr1984.firebasesample.extensions.toast
import com.tr1984.firebasesample.extensions.uiSubscribeWithError

class RepliesActivity: AppCompatActivity() {

    private lateinit var binding: ActivityRepliesBinding
    private lateinit var viewModel: RepliesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = RepliesViewModel()
        binding = ActivityRepliesBinding.inflate(layoutInflater)
        binding.activity = this
        binding.viewModel = viewModel
        setContentView(binding.root)

        setupUI()
        subscribeViewModel()

        viewModel.start(intent.getStringExtra("feedId"))
    }

    private fun setupUI() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter =
            RepliesAdapter(viewModel.items)

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
        }
    }
}