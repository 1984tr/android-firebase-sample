package com.tr1984.firebasesample.ui.feeds

import androidx.databinding.ObservableField

class FeedViewModel {

    var title = ObservableField("")
    var message = ObservableField("")
    var isOwner = ObservableField(false)
    var actionDelete: (() -> Unit)? = null
}