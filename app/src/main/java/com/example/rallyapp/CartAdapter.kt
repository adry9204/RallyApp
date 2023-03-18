package com.example.rallyapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rallyapp.api.dataModel.response_models.Cart
import com.example.rallyapp.user.UserCredentials
import com.example.rallyapp.viewModel.CartActivityViewModel
import com.squareup.picasso.Picasso

class CartAdapter(
    private var cartItems: MutableList<Cart>,
    private val viewModel: CartActivityViewModel
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    private var lastDeletedPos: Int? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardImage: ImageView = itemView.findViewById(R.id.shopping_cart_cardview_image)
        val cardTitle: TextView = itemView.findViewById(R.id.gridview_image_title)
        val cardPrice: TextView = itemView.findViewById(R.id.shopping_cart_cardview_price)
        val quantityLabel: TextView = itemView.findViewById(R.id.quantity_label)
        val deleteButton: ImageButton = itemView.findViewById(R.id.shopping_cart_delete_button_image)
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

        holder.deleteButton.setOnClickListener{
            lastDeletedPos = holder.adapterPosition
            viewModel.removeCartItem(cartItems[position].id, UserCredentials.getToken()!!)
        }
    }

    fun setData(newData: MutableList<Cart>){
        cartItems = newData
    }

    fun onDeleteFailed(){
        lastDeletedPos = null
    }

    fun onDeleteSuccess(){
        lastDeletedPos?.let {
            deleteItemAtPos(it)
            lastDeletedPos = null
        }
    }

    private fun deleteItemAtPos(pos: Int){
        cartItems.removeAt(pos)
        notifyItemRemoved(pos)
        notifyItemRangeChanged(pos, cartItems.size)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }
}