package com.tr1984.firebasesample.ui.feeds

import android.view.View
import android.view.ViewGroup
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.RecyclerView
import com.tr1984.firebasesample.data.Feed

class FeedsAdapter : RecyclerView.Adapter<FeedsAdapter.FeedsHolder>() {

    val items = ObservableArrayList<Feed>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedsHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: FeedsHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun bind() {

    }

    class FeedsHolder(val view: View) : RecyclerView.ViewHolder(view) {

        init {

        }

        fun bind(feed: Feed) {

        }
    }
}