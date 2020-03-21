package com.tr1984.firebasesample.ui.feeds

import androidx.databinding.ObservableField
import com.tr1984.firebasesample.data.Feed
import com.tr1984.firebasesample.extensions.disposeBag
import com.tr1984.firebasesample.extensions.uiSubscribe
import com.tr1984.firebasesample.firebase.FireStorageHelper
import com.tr1984.firebasesample.firebase.FirestoreHelper
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

class FeedWriteViewModel {

    var toastSubjet = PublishSubject.create<String>()
    var title = ObservableField("")
    var message = ObservableField("")
    var path = ""

    private var compositeDisposable = CompositeDisposable()

    fun destroy() {
        compositeDisposable.clear()
    }

    fun submit() {
        val title = title.get() ?: ""
        if (title.isEmpty()) {
            toastSubjet.onNext("제목을 입력해주세요.")
            return
        }
        val message = message.get() ?: ""
        if (message.isEmpty()) {
            toastSubjet.onNext("내용을 입력해주세요.")
            return
        }

        Single.just(path)
            .flatMap {
                if (it.isEmpty()) {
                    Single.just("")
                } else {
                    FireStorageHelper.upload(it)
                }
            }.map { Feed(0, "", title, message, it, null) }
            .flatMapCompletable { FirestoreHelper.instance.insertFeed(it) }
            .uiSubscribe({
                //TODO
            }, {
                it.printStackTrace()
                //TODO
            }).disposeBag(compositeDisposable)
    }


}