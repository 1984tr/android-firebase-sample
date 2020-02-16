package com.tr1984.firebasesample.ui

import android.graphics.Color
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.CircleOverlay
import com.tr1984.firebasesample.firebase.FirestoreHelper
import com.tr1984.firebasesample.firebase.MapFirebaseMessagingService
import com.tr1984.firebasesample.firebase.RemoteConfigHelper
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

class MapsViewModel : ViewModel() {

    var positionSubject = PublishSubject.create<LatLng>()
    var circleDrawSubject = PublishSubject.create<CircleOverlay>()
    var title = ObservableField("")
    var dataSource = ObservableField("")
    var lastUpdatedAt = ObservableField("")
    var contact = ObservableField("")
    var poiMainTitle = ObservableField("")

    private var compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
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
        RemoteConfigHelper.instance.run {
            title.set(getString(RemoteConfigHelper.Key.MAIN_TITLE))
            dataSource.set(getString(RemoteConfigHelper.Key.DATA_SOURCE))
            lastUpdatedAt.set(getString(RemoteConfigHelper.Key.LAST_UPDATED_AT))
            contact.set(getString(RemoteConfigHelper.Key.CONTACT))
            poiMainTitle.set(getString(RemoteConfigHelper.Key.POI_MAIN_TITLE))

            val initialMapPoint = get(RemoteConfigHelper.Key.INITIAL_MAP_POINT, HashMap::class.java)
            val latitude = initialMapPoint["latitude"] as Double
            val longitude = initialMapPoint["longitude"] as Double
            positionSubject.onNext(LatLng(latitude, longitude))

            circleDrawSubject.onNext(getCircleOverlay(LatLng(latitude, longitude), Color.RED))
        }
        loadData()
        checkFcmId()
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

    private fun getCircleOverlay(latLng: LatLng, color: Int): CircleOverlay {
        return CircleOverlay().apply {
            this.center = latLng
            this.radius = 20.0 // m
            this.color = color
            this.outlineWidth = 2 // px
            this.outlineColor = Color.BLACK
        }
    }
}