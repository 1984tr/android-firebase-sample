package com.tr1984.firebasesample.data

import androidx.annotation.Keep
import java.util.*

@Keep
data class Feed(
    var documentPath: String = "",
    val ownerUid: String = "",
    var status: Int = 0,
    val title: String = "",
    val message: String? = null,
    val imageUrl: String? = null,
    val replyCount: Int = 0,
    val replies: List<Reply>? = null,
    var time: Date = Date()
)