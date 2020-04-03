package com.tr1984.firebasesample.ui.feeds

import androidx.databinding.ObservableField

class FeedViewModel {

    var title = ObservableField("")
    var message = ObservableField("")
    var imageUrl = ObservableField("")
    var replyCount = ObservableField("")
    var isOwner = ObservableField(false)
    var actionClick: (() -> Unit)? = null
    var actionDelete: (() -> Unit)? = null
}