package com.tr1984.firebasesample.ui.feeds

import androidx.databinding.ObservableField
import io.reactivex.disposables.CompositeDisposable

class FeedWriteViewModel {

    var title = ObservableField("")
    var message = ObservableField("")

    private var compositeDisposable = CompositeDisposable()

    fun destroy() {
        compositeDisposable.clear()
    }

    fun submit() {

    }
}