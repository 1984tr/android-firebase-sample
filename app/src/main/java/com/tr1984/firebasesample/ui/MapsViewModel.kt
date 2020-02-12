package com.tr1984.firebasesample.ui

import androidx.lifecycle.ViewModel
import com.tr1984.firebasesample.firebase.FirestoreHelper
import com.tr1984.firebasesample.firebase.MapFirebaseMessagingService
import com.tr1984.firebasesample.firebase.RemoteConfigHelper
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

class MapsViewModel : ViewModel() {

    val readySubject = PublishSubject.create<Unit>()

    private var compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun start() {
        loadConfiguration()
    }

    private fun loadConfiguration() {
        RemoteConfigHelper.instance.fetchAndActivate {
            readySubject.onNext(Unit)
        }
    }

    private fun loadData() {
        FirestoreHelper.instance.loadData {

        }
    }

    private fun checkFcmId() {
        MapFirebaseMessagingService.getInstanceId {
            // TODO add realtimedb with auth
        }
    }
}