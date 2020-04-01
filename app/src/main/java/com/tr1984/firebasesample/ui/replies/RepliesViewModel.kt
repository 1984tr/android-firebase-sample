package com.tr1984.firebasesample.ui.replies

import androidx.databinding.ObservableField
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
    var reReplyPopupSubject = PublishSubject.create<((String) -> Unit)>()
    var compositeDisposable = CompositeDisposable()
    var items = arrayListOf<ReplyViewModel>()
    var replyText = ObservableField("")

    private var myUid = ""
    private var feedDocumentPath = ""

    init {
        myUid = Preferences.getString(Preferences.Key.Uid) ?: ""
    }

    fun start(feedDocumentPath: String? = null) {
        feedDocumentPath ?: return

        this.feedDocumentPath = feedDocumentPath
        this.items.clear()

        FirestoreHelper.instance.getReplies(feedDocumentPath)
            .uiSubscribe({ replies ->
                applyReplies(replies)
                updateSubject.onNext(Unit)
            }, {
                it.printStackTrace()
                toastSubject.onNext("잠시 후 다시 시도해주세요 :(")
            }).disposeBag(compositeDisposable)
    }

    fun submit() {
        val reply = replyText.get() ?: ""
        if (reply.isEmpty()) {
            toastSubject.onNext("댓글을 입력해주세요 :)")
        } else {
            insertReply(Reply().apply {
                ownerUid = myUid
                message = reply
                time = Date(System.currentTimeMillis())
            })
        }
    }

    private fun applyReplies(replies: List<Reply>) {
        replies.forEach { reply ->
            items.add(ReplyViewModel().apply {
                this.isReReply = false
                this.reply = reply.message
                this.isOwner = myUid == reply.ownerUid
                this.actionClick = {
                    reReplyPopupSubject.onNext { text ->
                        insertReReply(reply.documentPath, ReReply().apply {
                            ownerUid = myUid
                            message = text
                            time = Date(System.currentTimeMillis())
                        })
                    }
                }
                this.actionDelete = {
                    deleteReply(reply.documentPath)
                }
            })
            applyReReplies(reply.documentPath, reply.replies)
        }
    }

    private fun applyReReplies(replyDocumentPath: String, reReplies: List<ReReply>) {
        reReplies.forEach { reReply ->
            items.add(ReplyViewModel().apply {
                this.isReReply = true
                this.reply = reReply.message
                this.isOwner = myUid == reReply.ownerUid
                this.actionDelete = {
                    deleteReReply(replyDocumentPath, reReply.documentPath)
                }
            })
        }
    }

    private fun insertReply(reply: Reply) {
        FirestoreHelper.instance.insertReply(feedDocumentPath, reply)
            .uiSubscribe({
                replyText.set("")
                start(feedDocumentPath)
            }, {
                it.printStackTrace()
                toastSubject.onNext("잠시 후 다시 시도해주세요 :(")
            }).disposeBag(compositeDisposable)
    }

    private fun deleteReply(replyDocumentPath: String) {
        FirestoreHelper.instance.deleteReply(feedDocumentPath, replyDocumentPath)
            .uiSubscribe({
                start(feedDocumentPath)
            }, {
                it.printStackTrace()
                toastSubject.onNext("잠시 후 다시 시도해주세요 :(")
            }).disposeBag(compositeDisposable)
    }

    private fun insertReReply(replyDocumentPath: String, reReply: ReReply) {
        FirestoreHelper.instance.insertReReply(feedDocumentPath, replyDocumentPath, reReply)
            .uiSubscribe({
                start(feedDocumentPath)
            }, {
                it.printStackTrace()
                toastSubject.onNext("잠시 후 다시 시도해주세요 :(")
            }).disposeBag(compositeDisposable)
    }

    private fun deleteReReply(replyDocumentPath: String, reReplyDocumentPath: String) {
        FirestoreHelper.instance.deleteReReply(feedDocumentPath, replyDocumentPath, reReplyDocumentPath)
            .uiSubscribe({
                start(feedDocumentPath)
            }, {
                it.printStackTrace()
                toastSubject.onNext("잠시 후 다시 시도해주세요 :(")
            }).disposeBag(compositeDisposable)
    }
}