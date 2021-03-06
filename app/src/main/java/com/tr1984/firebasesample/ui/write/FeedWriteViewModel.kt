package com.tr1984.firebasesample.ui.write

import androidx.databinding.ObservableField
import com.tr1984.firebasesample.data.Feed
import com.tr1984.firebasesample.extensions.disposeBag
import com.tr1984.firebasesample.extensions.uiSubscribe
import com.tr1984.firebasesample.firebase.AnalyticsHelper
import com.tr1984.firebasesample.firebase.FireStorageHelper
import com.tr1984.firebasesample.firebase.FirestoreHelper
import com.tr1984.firebasesample.util.Preferences
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import java.util.*

class FeedWriteViewModel(val compositeDisposable: CompositeDisposable) {

    var toastSubject = PublishSubject.create<String>()
    var uploadCompleteSubject = PublishSubject.create<Unit>()
    var progressSubject = PublishSubject.create<Boolean>()
    var title = ObservableField("")
    var message = ObservableField("")
    var path = ""

    fun submit() {
        progressSubject.onNext(true)

        val title = title.get() ?: ""
        if (title.isEmpty()) {
            toastSubject.onNext("제목을 입력해주세요.")
            return
        }
        val message = message.get() ?: ""
        if (message.isEmpty()) {
            toastSubject.onNext("내용을 입력해주세요.")
            return
        }
        val uid = Preferences.getString(Preferences.Key.Uid) ?: ""
        if (uid.isEmpty()) {
            toastSubject.onNext("잠시 후 다시 시도해주세요 :(")
            return
        }

        AnalyticsHelper.instance.logEvent("submit_feed", "title" to title, "message" to message, "uid" to uid)

        Single.just(path)
            .flatMap {
                if (it.isEmpty()) {
                    Single.just("")
                } else {
                    FireStorageHelper.upload(it)
                }
            }.map {
                Feed(
                    ownerUid = uid,
                    status = 0,
                    title = title,
                    message = message,
                    imageUrl = it,
                    replyCount = 0,
                    replies = null,
                    time = Date(System.currentTimeMillis())
                )
            }.flatMapCompletable {
                FirestoreHelper.instance.insertFeed(it)
            }.doFinally {
                progressSubject.onNext(false)
            }.uiSubscribe({
                toastSubject.onNext("게시가 완료 되었습니다. :)")
                uploadCompleteSubject.onNext(Unit)
            }, {
                it.printStackTrace()
                toastSubject.onNext("잠시 후 다시 시도해주세요 :(")
            }).disposeBag(compositeDisposable)
    }


}