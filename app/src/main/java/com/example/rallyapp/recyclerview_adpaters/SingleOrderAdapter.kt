package com.example.rallyapp.recyclerview_adpaters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rallyapp.R
import com.example.rallyapp.api.dataModel.response_models.OrderDetail
import com.squareup.picasso.Picasso

class SingleOrderAdapter(
    private var orderDetails: List<OrderDetail>,
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
        with(holder) {
            with(orderDetails[position]){
                Picasso.get().load(menu.image).into(cardImage)
                cardTitle.text = menu.name
                cardPrice.text = "$${menu.price}"
            }
        }
    }


    fun setData(newData: List<OrderDetail>){
        orderDetails = newData
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return orderDetails.size
    }
}