package com.tr1984.firebasesample.data

import androidx.annotation.Keep
import java.util.*

@Keep
data class Reply(
    var documentPath: String = "",
    var ownerUid: String = "",
    var message: String = "",
    var replies: ArrayList<ReReply> = arrayListOf(),
    var time: Date = Date()
)