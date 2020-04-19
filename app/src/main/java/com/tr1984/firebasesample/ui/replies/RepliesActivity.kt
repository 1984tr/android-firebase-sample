package com.tr1984.firebasesample.ui.replies

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tr1984.firebasesample.databinding.ActivityRepliesBinding
import com.tr1984.firebasesample.extensions.disposeBag
import com.tr1984.firebasesample.extensions.toast
import com.tr1984.firebasesample.extensions.uiSubscribeWithError
import com.tr1984.firebasesample.firebase.AnalyticsHelper
import com.tr1984.firebasesample.ui.dialogs.ReReplyPopup
import io.reactivex.disposables.CompositeDisposable

class RepliesActivity: AppCompatActivity() {

    private lateinit var binding: ActivityRepliesBinding
    private lateinit var viewModel: RepliesViewModel
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AnalyticsHelper.instance.trackScreen(this)

        viewModel = RepliesViewModel(compositeDisposable)
        binding = ActivityRepliesBinding.inflate(layoutInflater)
        binding.activity = this
        binding.viewModel = viewModel
        setContentView(binding.root)

        bindModel()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    private fun bindModel() {
        viewModel.run {
            toastSubject
                .uiSubscribeWithError {
                    toast(it)
                }.disposeBag(compositeDisposable)
            reReplyPopupSubject
                .uiSubscribeWithError {
                    ReReplyPopup(this@RepliesActivity, it).show()
                }.disposeBag(compositeDisposable)

            start(intent.getStringExtra("feedDocumentPath"))
        }
    }
}