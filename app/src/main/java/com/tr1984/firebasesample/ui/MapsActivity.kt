package com.tr1984.firebasesample.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.tr1984.firebasesample.R
import com.tr1984.firebasesample.firebase.AnalyticsHelper
import com.tr1984.firebasesample.firebase.RemoteConfigHelper
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

class MapsActivity : AppCompatActivity() {

    private lateinit var googleMap: GoogleMap
    private var compositeDisposable = CompositeDisposable()
    private var readySubject = PublishSubject.create<Unit>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AnalyticsHelper.instance.trackScreen(this)

        setContentView(R.layout.activity_maps)

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
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), 9f))

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
