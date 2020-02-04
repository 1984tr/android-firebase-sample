package com.tr1984.firebasesample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

//        samplePoi.forEach {
//            val latlng = LatLng(it.lat, it.lng)
//            mMap.addMarker(MarkerOptions().position(latlng).title(it.title))
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 9f))
//        }
    }

//    private val samplePoi = arrayListOf(
//        Poi(37.37478492230988, 126.72695961530327, "Baegot"),
//        Poi(37.41179071428517, 126.90895676504908, "SeokSu"),
//        Poi(37.51224691265058, 127.11876347974098, "Bang-i"))
}
