package com.tr1984.firebasesample.ui.main

import android.net.Uri
import android.view.View
import androidx.databinding.ObservableField
import com.naver.maps.geometry.LatLng
import com.tr1984.firebasesample.BuildConfig
import com.tr1984.firebasesample.data.Pois
import com.tr1984.firebasesample.extensions.disposeBag
import com.tr1984.firebasesample.extensions.uiSubscribe
import com.tr1984.firebasesample.firebase.FirestoreHelper
import com.tr1984.firebasesample.firebase.FirebaseSampleMessagingService
import com.tr1984.firebasesample.firebase.RemoteConfigHelper
import com.tr1984.firebasesample.util.Logger
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

class MapsViewModel(val compositeDisposable: CompositeDisposable) {

    var shareSubject = PublishSubject.create<Uri>()
    var positionSubject = PublishSubject.create<LatLng>()
    var poisSubject = PublishSubject.create<Pois>()
    var poiGroupsSubject = PublishSubject.create<List<Pois>>()
    var title = ObservableField("")
    var dataSource = ObservableField("")
    var lastUpdatedAt = ObservableField("")
    var contact = ObservableField("")
    var poiMainTitle = ObservableField("")
    var versionText = ObservableField("")
    var poiCount = ObservableField("0")
    var poiListVisibility = ObservableField(View.GONE)
    var isExtendList = ObservableField(false)
    var actionExtension = {
        isExtendPois = !isExtendPois
    }

    var pois = listOf<Pois>()

    private var isExtendPois = false
        set(value) {
            field = value
            isExtendList.set(isExtendPois)
            poiListVisibility.set(
                if (value) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            )
        }

    fun getConfiguration(): Observable<Unit> {
        return Observable.create<Unit> { emit ->
            RemoteConfigHelper.instance.fetchAndActivate {
                emit.onNext(Unit)
                emit.onComplete()
            }
        }
    }

    fun start() {
        versionText.set("Ver. ${BuildConfig.VERSION_NAME}")

        RemoteConfigHelper.instance.run {
            title.set(getString(RemoteConfigHelper.Key.MAIN_TITLE))
            dataSource.set(getString(RemoteConfigHelper.Key.DATA_SOURCE))
            lastUpdatedAt.set(getString(RemoteConfigHelper.Key.LAST_UPDATED_AT))
            contact.set(getString(RemoteConfigHelper.Key.CONTACT))
            poiMainTitle.set(getString(RemoteConfigHelper.Key.POI_MAIN_TITLE))
        }
        loadData()
        checkFcmId()
    }

    private fun loadData() {
        FirestoreHelper.instance.getPois()
            .uiSubscribe({
                Logger.d("$it")

                pois = it

                poiCount.set("${it.size}")
                poiGroupsSubject.onNext(it)

                it.forEach { pois ->
                    poisSubject.onNext(pois)
                }
            }, {
                it.printStackTrace()
            }).disposeBag(compositeDisposable)
    }

    private fun checkFcmId() {
        FirebaseSampleMessagingService.getInstanceId {
            // TODO add realtimedb with auth
        }
    }
}