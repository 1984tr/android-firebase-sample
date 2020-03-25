package com.tr1984.firebasesample.ui.replies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.tr1984.firebasesample.R
import com.tr1984.firebasesample.databinding.ViewItemReplyBinding
import com.tr1984.firebasesample.databinding.ViewItemRereplyBinding

class RepliesAdapter(private val items: List<ReplyViewModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
                holder.binding.viewModel = items[position]
            }
            is ReReplyHolder -> {
                holder.binding.viewModel = items[position]
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

    companion object {
        const val TYPE_REPLY = 0
        const val TYPE_REREPLY = 1
    }
}