package com.tr1984.firebasesample.data

import android.graphics.Color
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.PathOverlay

data class Pois(val name: String? = "", val items: ArrayList<Poi> = arrayListOf<Poi>()) {

    val paths by lazy {
        PathOverlay().apply {
            width = 10
            outlineWidth = 0
            color = Color.parseColor(if (items.isNotEmpty()) (items[0].color ?: "#ff0000") else "#ff0000")
            if (items.size > 1) {
                coords = items.map { LatLng(it.point.latitude, it.point.longitude) }.toList()
            }
        }
    }
}