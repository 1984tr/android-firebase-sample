package com.tr1984.firebasesample.data

import androidx.annotation.Keep

@Keep
data class Feed(
    val ownerUid: String = "",
    val title: String = "",
    val message: String? = null,
    val imageUrl: String? = null,
    val replies: List<Reply>? = null
)