package com.tr1984.firebasesample.data

import androidx.annotation.Keep

@Keep
data class Reply(
    var id: String = "",
    val ownerUid: String = "",
    val message: String = "",
    var replies: ArrayList<ReReply> = arrayListOf()
)