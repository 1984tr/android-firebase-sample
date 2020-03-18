package com.tr1984.firebasesample.ui.feeds

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

class FeedWriteViewModel : ViewModel() {

    private var compositeDisposable = CompositeDisposable()

    fun start() {

    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}