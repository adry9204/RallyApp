package com.example.rallyapp.recyclerview_adpaters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rallyapp.R
import com.example.rallyapp.api.dataModel.response_models.Cart
import com.example.rallyapp.user.UserCredentials
import com.example.rallyapp.utils.AlertData
import com.example.rallyapp.utils.AlertManager
import com.example.rallyapp.utils.NetworkHelper
import com.example.rallyapp.viewModel.CartActivityViewModel
import com.squareup.picasso.Picasso

class CartAdapter(
    private var cartItems: MutableList<Cart>,
    private val viewModel: CartActivityViewModel,
    private val context: Context,
    private val showLoading: () -> Unit,
    private val onQuantityUpdate: (position: Int, quantity: Int) -> Unit
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    var lastDeletedPos: Int? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardImage: ImageView = itemView.findViewById(R.id.shopping_cart_cardview_image)
        val cardTitle: TextView = itemView.findViewById(R.id.gridview_image_title)
        val cardPrice: TextView = itemView.findViewById(R.id.shopping_cart_cardview_price)
        val quantityLabel: TextView = itemView.findViewById(R.id.quantity_label)
        val deleteButton: ImageButton = itemView.findViewById(R.id.shopping_cart_delete_button_image)
        val quantityIncreaseButton: Button = itemView.findViewById(R.id.plus_button)
        val quantityDecreaseButton: Button = itemView.findViewById(R.id.minus_button)
        val statusTextView: TextView = itemView.findViewById(R.id.shopping_cart_cart_status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_item_layout, parent, false)
        return ViewHolder(v)
    }

    private fun getTotalPrice(cartItem: Cart): String{
        return (cartItem.menu.price.toFloat() * cartItem.quantity).toString()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(cartItems[position].menu.image).into(holder.cardImage)
        holder.cardTitle.text = cartItems[position].menu.name
        holder.cardPrice.text = getTotalPrice(cartItems[position])
        holder.quantityLabel.text = "${cartItems[position].quantity}"
        if(cartItems[position].pending){
            holder.statusTextView.text = "PENDING \u26A0"
        }

        holder.deleteButton.setOnClickListener{
            val networkHelper = NetworkHelper(context)
            if(networkHelper.isConnectedToNetwork()){
                lastDeletedPos = holder.absoluteAdapterPosition
                showLoading()
                viewModel.removeCartItem(cartItems[position].id, UserCredentials.getToken()!!)
            }else {
                actionRestrictedWithOutNetworkAlert()
            }
        }

        holder.quantityIncreaseButton.setOnClickListener {
            val networkHelper = NetworkHelper(context)
            if(networkHelper.isConnectedToNetwork()){
                if(cartItems[position].quantity >= 10){
                    return@setOnClickListener
                }
                cartItems[position].quantity += 1
                viewModel.updateUserCart(
                    cartId = cartItems[position].id,
                    quantity = cartItems[position].quantity,
                    UserCredentials.getToken()!!
                )
                holder.quantityLabel.text = cartItems[position].quantity.toString()
                holder.cardPrice.text = getTotalPrice(cartItems[position])
                onQuantityUpdate(position, cartItems[position].quantity)
            }else{
                actionRestrictedWithOutNetworkAlert()
            }
        }

        holder.quantityDecreaseButton.setOnClickListener {
            val networkHelper = NetworkHelper(context)
            if(networkHelper.isConnectedToNetwork()){
                if(cartItems[position].quantity <= 1){
                    return@setOnClickListener
                }
                cartItems[position].quantity -= 1
                viewModel.updateUserCart(
                    cartId = cartItems[position].id,
                    quantity =  cartItems[position].quantity,
                    UserCredentials.getToken()!!
                )
                holder.quantityLabel.text = cartItems[position].quantity.toString()
                holder.cardPrice.text = getTotalPrice(cartItems[position])
            }else{
               actionRestrictedWithOutNetworkAlert()
            }
        }
    }

    fun actionRestrictedWithOutNetworkAlert(){
        val alertManager = AlertManager(context)
        val alertData = AlertData(
            title = "Cannot Perform Action",
            message = "The action cannot be performed without connecting to the internet"
        )
        alertManager.showAlertWithOkButton(alertData)
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