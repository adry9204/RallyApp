package com.example.rallyapp.recyclerview_adpaters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rallyapp.R
import com.squareup.picasso.Picasso

class SingleOrderAdapter(
    private val cardImages: Array<String>,
    private val cardTitles: Array<String>,
    private val cardPrices: Array<String>,
) : RecyclerView.Adapter<SingleOrderAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardImage: ImageView = itemView.findViewById(R.id.gridview_image)
        val cardTitle: TextView = itemView.findViewById(R.id.gridview_image_title)
        val cardPrice: TextView = itemView.findViewById(R.id.gridview_image_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(cardImages[position]).into(holder.cardImage)
        holder.cardTitle.text = cardTitles[position]
        holder.cardPrice.text = cardPrices[position]
    }

    override fun getItemCount(): Int {
        return cardImages.size
    }
}