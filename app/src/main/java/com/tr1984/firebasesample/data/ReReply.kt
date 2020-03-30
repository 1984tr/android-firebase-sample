package com.tr1984.firebasesample.data

import androidx.annotation.Keep
import java.util.*

@Keep
data class ReReply(var id: String = "", val ownerUid: String = "", val message: String = "", var time: Date = Date())