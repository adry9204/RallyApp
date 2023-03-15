package com.example.rallyapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rallyapp.dataModel.Cart
import com.squareup.picasso.Picasso

class CartAdapter(
    private val cartItems: List<Cart>
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardImage: ImageView = itemView.findViewById(R.id.shopping_cart_cardview_image)
        val cardTitle: TextView = itemView.findViewById(R.id.gridview_image_title)
        val cardPrice: TextView = itemView.findViewById(R.id.shopping_cart_cardview_price)
        val quantityLabel: TextView = itemView.findViewById(R.id.quantity_label)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_item_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(cartItems[position].menu.image).into(holder.cardImage)
        holder.cardTitle.text = cartItems[position].menu.name
        holder.cardPrice.text = "$${cartItems[position].price}"
        holder.quantityLabel.text = "${cartItems[position].quantity}"
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }
}