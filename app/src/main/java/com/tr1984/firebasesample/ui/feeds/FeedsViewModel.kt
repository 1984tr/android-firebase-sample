package com.tr1984.firebasesample.ui.feeds

import androidx.lifecycle.ViewModel
import com.tr1984.firebasesample.extensions.disposeBag
import com.tr1984.firebasesample.extensions.uiSubscribe
import com.tr1984.firebasesample.firebase.FirestoreHelper
import com.tr1984.firebasesample.util.Preferences
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

class FeedsViewModel : ViewModel() {

    var compositeDisposable = CompositeDisposable()
    var toastSubject = PublishSubject.create<String>()
    var items = arrayListOf<FeedViewModel>()
    var updateSubject = PublishSubject.create<Unit>()

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
                        replyCount.set("댓글 ${feed.replies?.size ?: 0}")
                        isOwner.set(feed.ownerUid == myUid)
                        actionDelete = {
                            deleteFeed(feed.id)
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