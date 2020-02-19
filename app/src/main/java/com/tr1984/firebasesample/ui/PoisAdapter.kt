package com.tr1984.firebasesample.ui

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tr1984.firebasesample.data.Pois

class PoisAdapter(val items: List<Pois>) : RecyclerView.Adapter<PoisAdapter.PoisViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoisViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: PoisViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    class PoisViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    }
}