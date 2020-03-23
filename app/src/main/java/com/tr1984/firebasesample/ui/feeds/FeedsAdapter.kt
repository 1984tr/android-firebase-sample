package com.tr1984.firebasesample.ui.feeds

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.tr1984.firebasesample.R
import com.tr1984.firebasesample.databinding.ViewItemFeedBinding

class FeedsAdapter(private val items: List<FeedViewModel>) : RecyclerView.Adapter<FeedsAdapter.FeedsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedsHolder {
        return FeedsHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_item_feed, parent, false))
    }

    override fun onBindViewHolder(holder: FeedsHolder, position: Int) {
        holder.binding.viewModel = items[position]
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class FeedsHolder(val view: View) : RecyclerView.ViewHolder(view) {

        var binding: ViewItemFeedBinding = DataBindingUtil.bind(view)!!
    }
}