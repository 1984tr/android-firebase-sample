package com.tr1984.firebasesample.ui.replies

class ReplyViewModel {

    var path = ""
    var isReReply = false
    var reply: String = ""
    var date: String = ""
    var isOwner: Boolean = false
    var actionClick : (() -> Unit)? = null
    var actionDelete: (() -> Unit)? = null
}