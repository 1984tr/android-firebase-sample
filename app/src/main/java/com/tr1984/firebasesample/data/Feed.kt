package com.tr1984.firebasesample.data

data class Feed(val id: Int, val ownerUid: String, val title: String, val message: String?, val imageUrl: String?, val replies: List<Reply>?)