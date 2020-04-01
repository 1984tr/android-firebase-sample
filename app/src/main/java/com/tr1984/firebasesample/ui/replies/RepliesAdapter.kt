package com.tr1984.firebasesample.ui.replies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tr1984.firebasesample.R
import com.tr1984.firebasesample.databinding.ViewItemReplyBinding
import com.tr1984.firebasesample.databinding.ViewItemRereplyBinding

class RepliesAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items = ObservableArrayList<ReplyViewModel>()

    fun bind(newItems: ObservableArrayList<ReplyViewModel>) {
        val callback = DiffCallback(items, newItems)
        val result = DiffUtil.calculateDiff(callback)

        items.clear()
        items.addAll(newItems)

        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_REREPLY) {
            ReReplyHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.view_item_rereply,
                    parent,
                    false
                )
            )
        } else {
            ReplyHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.view_item_reply, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ReplyHolder -> {
                holder.binding.run {
                    viewModel = items[position]
                    executePendingBindings()
                }
            }
            is ReReplyHolder -> {
                holder.binding.run {
                    viewModel = items[position]
                    executePendingBindings()
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position].isReReply) {
            TYPE_REREPLY
        } else {
            TYPE_REPLY
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ReplyHolder(val view: View) : RecyclerView.ViewHolder(view) {

        var binding: ViewItemReplyBinding = DataBindingUtil.bind(view)!!
    }

    class ReReplyHolder(val view: View) : RecyclerView.ViewHolder(view) {

        var binding: ViewItemRereplyBinding = DataBindingUtil.bind(view)!!
    }

    inner class DiffCallback(
        private val oldList: List<ReplyViewModel>,
        private val newList: List<ReplyViewModel>
    ) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].path == newList[newItemPosition].path
        }

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    companion object {
        const val TYPE_REPLY = 0
        const val TYPE_REREPLY = 1
    }
}