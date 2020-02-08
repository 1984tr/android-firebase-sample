package com.tr1984.firebasesample.data

import com.google.firebase.firestore.GeoPoint

data class Poi(
    val point: GeoPoint = GeoPoint(0.0, 0.0),
    val title: String = "",
    val extras: List<String> = listOf()
)