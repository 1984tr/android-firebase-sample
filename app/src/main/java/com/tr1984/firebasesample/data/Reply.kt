package com.tr1984.firebasesample.data

data class Reply(val id: Int, val ownerUid: String, val message: String, val replies: List<ReReply>?)