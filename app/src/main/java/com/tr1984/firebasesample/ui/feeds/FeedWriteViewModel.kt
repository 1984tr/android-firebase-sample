package com.tr1984.firebasesample.ui.feeds

import io.reactivex.disposables.CompositeDisposable

class FeedWriteViewModel {

    private var compositeDisposable = CompositeDisposable()

    fun start() {

    }

    fun destroy() {
        compositeDisposable.clear()
    }
}