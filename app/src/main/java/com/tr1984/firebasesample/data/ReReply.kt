package com.tr1984.firebasesample.data

import androidx.annotation.Keep

@Keep
data class ReReply(val id: Int, val replyId: Int, val ownerUid: String, val message: String)