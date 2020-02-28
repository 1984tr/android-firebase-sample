package com.tr1984.firebasesample.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tr1984.firebasesample.R
import com.tr1984.firebasesample.data.Pois

class PoisAdapter(private val items: List<Pois>, private val clickItem: (Pois) -> Unit) : RecyclerView.Adapter<PoisAdapter.PoisViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoisViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_item_pois, parent, false)
        return PoisViewHolder(view)
    }

    override fun onBindViewHolder(holder: PoisViewHolder, position: Int) {
        val pois = items[position]
        holder.nameText.text = pois.name
        holder.nameText.setOnClickListener {
            clickItem.invoke(pois)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class PoisViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameText = itemView.findViewById<TextView>(R.id.txt_label)
    }
}