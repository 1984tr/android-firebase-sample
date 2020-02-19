package com.tr1984.firebasesample.data

import android.graphics.Color
import com.google.firebase.firestore.GeoPoint
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.CircleOverlay
import com.naver.maps.map.overlay.InfoWindow

data class Poi(
    val point: GeoPoint = GeoPoint(0.0, 0.0),
    val color: String? = null,
    val title: String = "",
    val extras: List<String> = listOf()
) {
    val circleOverlay by lazy {
        CircleOverlay().apply {
            this.center = LatLng(point.latitude, point.longitude)
            this.radius = 20.0 // m
            this.color = Color.parseColor(this@Poi.color ?: "#ff0000")
            this.outlineWidth = 2 // px
            this.outlineColor = Color.parseColor(this@Poi.color ?: "#ff0000")
        }
    }
    
    val infoWindow by lazy {
        InfoWindow().apply {
            position = LatLng(point.latitude, point.longitude)
        }
    }

    fun getMessage() : String {
        return "$title\n${extras.joinToString("\n")}"
    }
}