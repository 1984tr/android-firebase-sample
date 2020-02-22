package com.tr1984.firebasesample.ui

import android.graphics.Color
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.CircleOverlay
import com.naver.maps.map.overlay.InfoWindow
import com.tr1984.firebasesample.BuildConfig
import com.tr1984.firebasesample.data.Pois
import com.tr1984.firebasesample.extensions.disposeBag
import com.tr1984.firebasesample.extensions.uiSubscribe
import com.tr1984.firebasesample.firebase.FirestoreHelper
import com.tr1984.firebasesample.firebase.MapFirebaseMessagingService
import com.tr1984.firebasesample.firebase.RemoteConfigHelper
import com.tr1984.firebasesample.util.Logger
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

class MapsViewModel : ViewModel() {

    var positionSubject = PublishSubject.create<LatLng>()
    var circleDrawSubject = PublishSubject.create<CircleOverlay>()
    var infoWindowSubject = PublishSubject.create<Pair<String, InfoWindow>>()
    var title = ObservableField("")
    var dataSource = ObservableField("")
    var lastUpdatedAt = ObservableField("")
    var contact = ObservableField("")
    var poiMainTitle = ObservableField("")
    var poiGroups = ObservableArrayList<Pois>()
    var versionText = ObservableField("")

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

        versionText.set("Ver. ${BuildConfig.VERSION_NAME}")
    }

    private fun loadData() {
        // TODO 전체 만들기
        // memory cache
        FirestoreHelper.instance.getPois()
            .uiSubscribe({
                Logger.d("$it")
                for (pois in it) {
                    for (poi in pois.items) {
                        circleDrawSubject.onNext(poi.circleOverlay)
                        infoWindowSubject.onNext(poi.getMessage() to poi.infoWindow)
                    }
                }

                // draw circles
                // draw popup
                // add child to list
            }, {

            }).disposeBag(compositeDisposable)
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