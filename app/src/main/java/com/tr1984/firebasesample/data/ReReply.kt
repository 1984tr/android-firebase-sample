package com.tr1984.firebasesample.data

import androidx.annotation.Keep

@Keep
data class ReReply(var id: String = "", val ownerUid: String = "", val message: String = "")