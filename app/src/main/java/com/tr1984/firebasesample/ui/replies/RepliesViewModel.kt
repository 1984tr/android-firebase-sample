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
                    replies.forEach { reply ->
                        val replyVM = ReplyViewModel().apply {
                            this.isReReply = false
                            this.reply = reply.message
                            this.isOwner = myUid == reply.ownerUid
                            this.actionClick = {
                                rereplyPopupSubject.onNext { text ->
                                    insertReReply(reply.id, ReReply().apply {
                                        ownerUid = myUid
                                        message = text
                                        time = Date(System.currentTimeMillis())
                                    })
                                }
                            }
                            this.actionDelete = {
                                deleteReply(reply.id)
                            }
                        }
                        items.add(replyVM)

                        reply.replies.forEach { rereply ->
                            val rereplyVM = ReplyViewModel().apply {
                                this.isReReply = true
                                this.reply = rereply.message
                                this.isOwner = myUid == rereply.ownerUid
                                this.actionDelete = {
                                    deleteReReply(reply.id, rereply.id)
                                }
                            }
                            items.add(rereplyVM)
                        }
                    }
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

    fun deleteReReply(replyPath: String, rereplyPath: String) {
        FirestoreHelper.instance.deleteReReply(feedId, replyPath, rereplyPath)
            .uiSubscribe({
                start(feedId)
            }, {
                it.printStackTrace()
                toastSubject.onNext("잠시 후 다시 시도해주세요 :(")
            }).disposeBag(compositeDisposable)
    }
}