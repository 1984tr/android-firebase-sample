package com.tr1984.firebasesample.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.tr1984.firebasesample.R
import com.tr1984.firebasesample.databinding.ActivityMapsBinding
import com.tr1984.firebasesample.extensions.disposeBag
import com.tr1984.firebasesample.extensions.uiSubscribe
import com.tr1984.firebasesample.firebase.AnalyticsHelper
import com.tr1984.firebasesample.firebase.RemoteConfigHelper
import io.reactivex.disposables.CompositeDisposable

class MapsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMapsBinding
    private lateinit var viewModel: MapsViewModel
    private var naverMap: NaverMap? = null

    private var compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AnalyticsHelper.instance.trackScreen(this)

        viewModel = MapsViewModel()
        subscribes(viewModel)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        setContentView(binding.root)

        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }
        mapFragment.getMapAsync(viewModel.mapReadyCallback)


        mapFragment.getMapAsync { naverMap ->
            this.naverMap = naverMap
            settingMap()
        }

//        readySubject.buffer(2)
//            .subscribe {
//                ready()
//            }.disposeBag(compositeDisposable)
//
//
//
//        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync { googleMap ->
//            this@MapsActivity.googleMap = googleMap
//            readySubject.onNext(Unit)
//
//            //        samplePoi.forEach {
//            //            val latlng = LatLng(it.lat, it.lng)
//            //            mMap.addMarker(MarkerOptions().position(latlng).title(it.title))
//            //            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 9f))
//            //        }
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    private fun subscribes(viewModel: MapsViewModel) {
        viewModel.readySubject
            .buffer(2)
            .uiSubscribe({

            }, {

            }).disposeBag(compositeDisposable)
    }

    private fun settingMap() {
        naverMap?.uiSettings?.run {
            isZoomControlEnabled = false
        }
    }

    private fun ready() {
        RemoteConfigHelper.instance.run {
            val title = getString(RemoteConfigHelper.Key.MAIN_TITLE)
            val dataSource = getString(RemoteConfigHelper.Key.DATA_SOURCE)
            val lastUpdatedAt = getString(RemoteConfigHelper.Key.LAST_UPDATED_AT)
            val contact = getString(RemoteConfigHelper.Key.CONTACT)
            val poiMainTitle = getString(RemoteConfigHelper.Key.POI_MAIN_TITLE)

            val initialMapPoint = get(RemoteConfigHelper.Key.INITIAL_MAP_POINT, HashMap::class.java)
            val latitude = initialMapPoint["latitude"] as Double
            val longitude = initialMapPoint["longitude"] as Double
//            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), 9f))

            Log.d(
                "trtr", "title: $title, dataSource: $dataSource, lastUpdatedAt: $lastUpdatedAt, " +
                        "contact: $contact, poiMainTitle: $poiMainTitle, latitude: $latitude, longitude: $longitude"
            )
        }
    }


//    private val samplePoi = arrayListOf(
//        Poi(37.37478492230988, 126.72695961530327, "Baegot"),
//        Poi(37.41179071428517, 126.90895676504908, "SeokSu"),
//        Poi(37.51224691265058, 127.11876347974098, "Bang-i"))
}
