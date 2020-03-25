package com.tr1984.firebasesample.ui.replies

import com.tr1984.firebasesample.util.Preferences
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

class RepliesViewModel {

    var compositeDisposable = CompositeDisposable()
    var toastSubject = PublishSubject.create<String>()
    var items = arrayListOf<ReplyViewModel>()
    var updateSubject = PublishSubject.create<Unit>()

    private var myUid = ""

    init {
        myUid = Preferences.getString(Preferences.Key.Uid) ?: ""
    }

    fun start() {

    }
}