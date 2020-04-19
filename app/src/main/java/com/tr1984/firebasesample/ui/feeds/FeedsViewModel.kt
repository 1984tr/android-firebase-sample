package com.tr1984.firebasesample.ui.feeds

import com.tr1984.firebasesample.data.Feed
import com.tr1984.firebasesample.extensions.disposeBag
import com.tr1984.firebasesample.extensions.uiSubscribe
import com.tr1984.firebasesample.firebase.FirestoreHelper
import com.tr1984.firebasesample.util.Preferences
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import java.text.SimpleDateFormat

class FeedsViewModel(val compositeDisposable: CompositeDisposable) {

    var toastSubject = PublishSubject.create<String>()
    var updateSubject = PublishSubject.create<Unit>()
    var showRepliesSubject = PublishSubject.create<Feed>()
    var items = arrayListOf<FeedViewModel>()

    private var myUid = ""

    init {
        myUid = Preferences.getString(Preferences.Key.Uid) ?: ""
    }

    fun start() {
        items.clear()
        FirestoreHelper.instance.getFeeds()
            .uiSubscribe({ list ->
                items.addAll(getFeedViewModels(list))
                updateSubject.onNext(Unit)
            }, {
                it.printStackTrace()
                toastSubject.onNext("잠시 후 다시 시도해주세요 :(")
            }).disposeBag(compositeDisposable)
    }

    private fun getFeedViewModels(feeds: List<Feed>) : List<FeedViewModel> {
        return feeds.map { feed ->
            FeedViewModel().apply {
                title.set(feed.title)
                message.set(feed.message)
                feed.imageUrl?.let {
                    imageUrl.set(it)
                }
                replyCount.set("댓글 ${feed.replyCount}")
                date.set(SimpleDateFormat("MM.dd HH:mm:ss").format(feed.time))
                isOwner.set(feed.ownerUid == myUid)
                actionClick = {
                    showRepliesSubject.onNext(feed)
                }
                actionDelete = {
                    deleteFeed(feed.documentPath)
                    items.remove(this)
                }
            }
        }
    }

    private fun deleteFeed(documentPath: String) {
        FirestoreHelper.instance.deleteFeed(documentPath)
            .uiSubscribe({
                updateSubject.onNext(Unit)
            }, {
                it.printStackTrace()
            }).disposeBag(compositeDisposable)
    }
}