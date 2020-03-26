package com.tr1984.firebasesample.ui.replies

import com.tr1984.firebasesample.util.Preferences
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

class RepliesViewModel {

    var toastSubject = PublishSubject.create<String>()
    var updateSubject = PublishSubject.create<Unit>()
    var compositeDisposable = CompositeDisposable()
    var items = arrayListOf<ReplyViewModel>()

    private var myUid = ""
    private var feedId = ""

    init {
        myUid = Preferences.getString(Preferences.Key.Uid) ?: ""
    }

    fun start(feedId: String? = null) {
        feedId?.let {
            this.feedId = it
        }
    }
}