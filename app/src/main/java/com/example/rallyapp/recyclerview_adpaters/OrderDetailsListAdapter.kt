package com.example.rallyapp.recyclerview_adpaters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rallyapp.R
import com.example.rallyapp.api.dataModel.response_models.OrderDetail
import com.squareup.picasso.Picasso

class OrderDetailsListAdapter(
    private var orderDetails: List<OrderDetail>
) : RecyclerView.Adapter<OrderDetailsListAdapter.ViewHolder>(){

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val orderDetailsImageView: ImageView = itemView.findViewById(R.id.order_items_list_item_image)
        val orderDetailItemName: TextView = itemView.findViewById(R.id.order_items_list_item_name)
        val orderDetailItemQuantity: TextView = itemView.findViewById(R.id.order_items_list_item_quantity)
        val orderDetailItemPrice: TextView = itemView.findViewById(R.id.order_items_list_item_price)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(orderDetails[position].menu.image).into(holder.orderDetailsImageView)
        holder.orderDetailItemName.text = orderDetails[position].menu.name
        holder.orderDetailItemQuantity.text = "${orderDetails[position].quantity}"
        holder.orderDetailItemPrice.text = "$${orderDetails[position].menu.price}"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.order_items_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return orderDetails.size
    }

    fun setData(newData: List<OrderDetail>){
        orderDetails = newData
        notifyDataSetChanged()
    }
}