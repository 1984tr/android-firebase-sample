package com.tr1984.firebasesample.extensions

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun Disposable.disposeBag(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}