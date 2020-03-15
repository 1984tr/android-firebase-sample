package com.tr1984.firebasesample.extensions

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.RecyclerView
import com.tr1984.firebasesample.data.Pois

@BindingAdapter("visibleIfTrue")
fun View.visibleIfTrue(visible: Boolean) {
    if (visible) {
        visibility = View.VISIBLE
    } else {
        visibility = View.GONE
    }
}


fun RecyclerView.bindPois(items: ObservableArrayList<Pois>) {

}