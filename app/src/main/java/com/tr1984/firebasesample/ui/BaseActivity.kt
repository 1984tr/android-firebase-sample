package com.tr1984.firebasesample.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.tr1984.firebasesample.BR
import com.tr1984.firebasesample.extensions.alert
import com.tr1984.firebasesample.extensions.disposeBag
import com.tr1984.firebasesample.extensions.toast
import com.tr1984.firebasesample.extensions.uiSubscribeWithError

abstract class BaseActivity<T : BaseViewModel, VB : ViewDataBinding> : AppCompatActivity() {

    abstract var viewModel: T
    abstract var layoutId: Int

    protected lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.setVariable(BR.viewModel, viewModel)
        binding.executePendingBindings()

        subscribeSubjects()
    }

    override fun onDestroy() {
        viewModel.compositeDisposable.clear()
        super.onDestroy()
    }

    open fun subscribeSubjects() {
        viewModel.run {
            startPageSubject
                .uiSubscribeWithError {
                    val intent = (it.intent ?: Intent()).apply {
                        setClass(this@BaseActivity, it.clazz)
                    }
                    if ((it.requestCode ?: 0) > 0) {
                        startActivityForResult(intent, it.requestCode ?: 0)
                    } else {
                        startActivity(intent)
                    }
                }.disposeBag(compositeDisposable)

            finishPageSubject
                .uiSubscribeWithError {
                    it.resultCode?.let { rc ->
                        setResult(rc, it.intent)
                        finish()
                    } ?: finish()
                }.disposeBag(compositeDisposable)

            popupSubject
                .uiSubscribeWithError {
                    this@BaseActivity.alert(
                        it.title,
                        it.message ?: "",
                        it.positiveLabel,
                        it.positiveCallback,
                        it.negativeLabel,
                        it.negativeCallback
                    )
                }.disposeBag(compositeDisposable)

            errorSubject
                .uiSubscribeWithError {
                    this@BaseActivity.alert(
                        message = it.throwable.localizedMessage,
                        positiveCallback = it.callback
                    )
                }.disposeBag(compositeDisposable)

            toastSubject
                .uiSubscribeWithError {
                    this@BaseActivity.toast(it)
                }.disposeBag(compositeDisposable)
        }
    }
}