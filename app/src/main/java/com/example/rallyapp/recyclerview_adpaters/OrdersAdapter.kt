package com.example.rallyapp.recyclerview_adpaters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rallyapp.R

class OrdersAdapter (
    private val cardDate: Array<String>,
    private val cardSummary: Array<String>,
) : RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderDate: TextView = itemView.findViewById(R.id.date_and_total_label)
        val orderSummary: TextView = itemView.findViewById(R.id.order_description_label)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_order_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.orderDate.text = cardDate[position]
        holder.orderSummary.text = cardSummary[position]
    }

    override fun getItemCount(): Int {
        return cardDate.size
    }
}