package com.tr1984.firebasesample.ui.feeds

import androidx.databinding.ObservableField
import com.tr1984.firebasesample.ui.BaseViewModel

class FeedViewModel : BaseViewModel() {

    var title = ObservableField("")
    var message = ObservableField("")
    var isOwner = ObservableField(false)
    var actionDelete: (() -> Unit)? = null
}