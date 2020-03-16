package com.tr1984.firebasesample.extensions

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("visibleIfTrue")
fun View.visibleIfTrue(visible: Boolean) {
    if (visible) {
        visibility = View.VISIBLE
    } else {
        visibility = View.GONE
    }
}