package com.tr1984.firebasesample.ui.feeds

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

class FeedsViewModel : ViewModel() {

    private var compositeDisposable = CompositeDisposable()

    var items = ObservableArrayList<FeedViewModel>()

    fun start() {
        items.add(FeedViewModel())
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}