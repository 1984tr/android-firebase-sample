package com.tr1984.firebasesample.extensions

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tr1984.firebasesample.ui.replies.RepliesAdapter
import com.tr1984.firebasesample.ui.replies.ReplyViewModel

@BindingAdapter("visibleIfTrue")
fun View.visibleIfTrue(visible: Boolean) {
    if (visible) {
        visibility = View.VISIBLE
    } else {
        visibility = View.GONE
    }
}

@BindingAdapter("bindReplies")
fun RecyclerView.bindReplies(items: ObservableArrayList<ReplyViewModel>) {
    if (adapter is RepliesAdapter) {
        (adapter as RepliesAdapter).bind(items)
    }
}

@BindingAdapter("loadImageUrl")
fun ImageView.loadImageUrl(url: String?) {
    if (url != null) {
        try {
            if (this.context == null) {
                return
            }
            Glide.with(this)
                .load(url)
                .into(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}