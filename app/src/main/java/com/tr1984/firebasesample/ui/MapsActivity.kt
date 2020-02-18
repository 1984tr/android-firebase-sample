package com.tr1984.firebasesample.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.InfoWindow
import com.tr1984.firebasesample.R
import com.tr1984.firebasesample.databinding.ActivityMapsBinding
import com.tr1984.firebasesample.extensions.alert
import com.tr1984.firebasesample.extensions.disposeBag
import com.tr1984.firebasesample.extensions.uiSubscribe
import com.tr1984.firebasesample.firebase.AnalyticsHelper
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction

class MapsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMapsBinding
    private lateinit var viewModel: MapsViewModel
    private var naverMap: NaverMap? = null

    private var compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AnalyticsHelper.instance.trackScreen(this)

        viewModel = MapsViewModel()
        subscribeViewModel()

        binding = ActivityMapsBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        setContentView(binding.root)

        Observable.combineLatest(
            getMap(),
            viewModel.getConfiguration(),
            BiFunction<NaverMap, Unit, NaverMap> { map, _ ->
                map
            })
            .uiSubscribe({
                this.naverMap = it
                settingMap()
                viewModel.start()
            }, {
                this@MapsActivity.alert("알림", it.localizedMessage)
            }).disposeBag(compositeDisposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    private fun getMap(): Observable<NaverMap> {
        return Observable.create<NaverMap> { emit ->
            val fm = supportFragmentManager
            val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
                ?: MapFragment.newInstance().also {
                    fm.beginTransaction().add(R.id.map, it).commit()
                }
            mapFragment.getMapAsync { naverMap ->
                emit.onNext(naverMap)
                emit.onComplete()
            }
        }
    }

    private fun settingMap() {
        naverMap?.uiSettings?.run {
            isZoomControlEnabled = false
        }
    }

    private fun subscribeViewModel() {
        viewModel.run {
            positionSubject
                .uiSubscribe({
                    naverMap?.moveCamera(CameraUpdate.scrollTo(it))
                }, {
                    it.printStackTrace()
                }).disposeBag(compositeDisposable)
            circleDrawSubject
                .uiSubscribe({
                    it.map = naverMap
                }, {
                    it.printStackTrace()
                }).disposeBag(compositeDisposable)
        }
    }

    private fun drawCircle() {

    }

    private fun openInfoWindow(text: String, pos: LatLng) {
        naverMap?.let { map ->
            val infoWindow = InfoWindow().apply {
                adapter = object : InfoWindow.DefaultTextAdapter(this@MapsActivity) {
                    override fun getText(infoWindow: InfoWindow): CharSequence {
                        return text
                    }
                }
                position = pos
            }
            infoWindow.open(map)
        }
    }
}
