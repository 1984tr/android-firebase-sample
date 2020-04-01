package com.tr1984.firebasesample.ui.feeds

import androidx.lifecycle.ViewModel
import com.tr1984.firebasesample.data.Feed
import com.tr1984.firebasesample.extensions.disposeBag
import com.tr1984.firebasesample.extensions.uiSubscribe
import com.tr1984.firebasesample.firebase.FirestoreHelper
import com.tr1984.firebasesample.util.Preferences
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

class FeedsViewModel : ViewModel() {

    var toastSubject = PublishSubject.create<String>()
    var updateSubject = PublishSubject.create<Unit>()
    var showRepliesSubject = PublishSubject.create<Feed>()
    var compositeDisposable = CompositeDisposable()
    var items = arrayListOf<FeedViewModel>()

    private var myUid = ""

    init {
        myUid = Preferences.getString(Preferences.Key.Uid) ?: ""
    }

    fun start() {
        items.clear()
        FirestoreHelper.instance.getFeeds()
            .uiSubscribe({ list ->
                list.forEach { feed ->
                    items.add(FeedViewModel().apply {
                        title.set(feed.title)
                        message.set(feed.message)
                        replyCount.set("댓글 ${feed.replyCount}")
                        isOwner.set(feed.ownerUid == myUid)
                        actionClick = {
                            showRepliesSubject.onNext(feed)
                        }
                        actionDelete = {
                            deleteFeed(feed.documentPath)
                            items.remove(this)
                        }
                    })
                }
                updateSubject.onNext(Unit)
            }, {
                it.printStackTrace()
                toastSubject.onNext("잠시 후 다시 시도해주세요 :(")
            }).disposeBag(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    private fun deleteFeed(path: String) {
        FirestoreHelper.instance.deleteFeed(path)
            .uiSubscribe({
                updateSubject.onNext(Unit)
            }, {
                it.printStackTrace()
            }).disposeBag(compositeDisposable)
    }
}