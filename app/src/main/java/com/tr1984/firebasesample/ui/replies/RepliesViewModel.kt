package com.tr1984.firebasesample.ui.replies

import com.tr1984.firebasesample.data.ReReply
import com.tr1984.firebasesample.data.Reply
import com.tr1984.firebasesample.extensions.disposeBag
import com.tr1984.firebasesample.extensions.uiSubscribe
import com.tr1984.firebasesample.firebase.FirestoreHelper
import com.tr1984.firebasesample.util.Preferences
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import java.util.*

class RepliesViewModel {

    var toastSubject = PublishSubject.create<String>()
    var updateSubject = PublishSubject.create<Unit>()
    var rereplyPopupSubject = PublishSubject.create<((String) -> Unit)>()
    var submitCompleteSubject = PublishSubject.create<Unit>()
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
            this.items.clear()
            FirestoreHelper.instance.getReplies(it)
                .uiSubscribe({ replies ->
                    items.addAll(replies.map { r ->
                        ReplyViewModel().apply {
                            isReReply = false
                            reply = r.message
                            isOwner = myUid == r.ownerUid
                            actionClick = {
                                rereplyPopupSubject.onNext { text ->
                                    insertReReply(r.id, ReReply().apply {
                                        ownerUid = myUid
                                        message = text
                                        time = Date(System.currentTimeMillis())
                                    })
                                }
                            }
                            actionDelete = {
                                deleteReply(r.id)
                            }
                        }
                    })
                    updateSubject.onNext(Unit)
                }, {
                    it.printStackTrace()
                    toastSubject.onNext("잠시 후 다시 시도해주세요 :(")
                }).disposeBag(compositeDisposable)
        }
    }

    fun submit(text: String) {
        FirestoreHelper.instance.insertReply(feedId, Reply().apply {
            ownerUid = myUid
            message = text
            time = Date(System.currentTimeMillis())
        }).uiSubscribe({
            submitCompleteSubject.onNext(Unit)
            start(feedId)
        }, {
            it.printStackTrace()
            toastSubject.onNext("잠시 후 다시 시도해주세요 :(")
        }).disposeBag(compositeDisposable)
    }

    fun deleteReply(replyPath: String) {
        FirestoreHelper.instance.deleteReply(feedId, replyPath)
            .uiSubscribe({
                start(feedId)
            }, {
                it.printStackTrace()
                toastSubject.onNext("잠시 후 다시 시도해주세요 :(")
            }).disposeBag(compositeDisposable)
    }

    fun insertReReply(replyPath: String, rereply: ReReply) {
        FirestoreHelper.instance.insertReReply(feedId, replyPath, rereply)
            .uiSubscribe({
                start(feedId)
            }, {
                it.printStackTrace()
                toastSubject.onNext("잠시 후 다시 시도해주세요 :(")
            }).disposeBag(compositeDisposable)
    }
}