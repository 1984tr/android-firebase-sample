package com.tr1984.firebasesample.data

import androidx.annotation.Keep

@Keep
data class Reply(
    val ownerUid: String = "",
    val message: String = "",
    var replies: ArrayList<ReReply> = arrayListOf()
)