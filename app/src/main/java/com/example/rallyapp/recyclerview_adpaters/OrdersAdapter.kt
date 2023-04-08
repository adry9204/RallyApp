package com.example.rallyapp.recyclerview_adpaters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rallyapp.R
import com.example.rallyapp.activities.SingleOrderActivity
import com.example.rallyapp.api.dataModel.response_models.Order
import com.example.rallyapp.api.dataModel.response_models.User
import com.example.rallyapp.user.UserCredentials
import com.example.rallyapp.utils.AlertData
import com.example.rallyapp.utils.AlertManager
import com.example.rallyapp.utils.NetworkHelper
import com.example.rallyapp.viewModel.OrderActivityViewModel

class OrdersAdapter (
    private val context: Context,
    private var orders: List<Order<User>>,
    private val viewModel: OrderActivityViewModel
) : RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderDate: TextView = itemView.findViewById(R.id.date_and_total_label)
        val orderSummary: TextView = itemView.findViewById(R.id.order_description_label)
        private val reorderButton: Button = itemView.findViewById(R.id.reorder_button)

        init {
            itemView.setOnClickListener{
                val intent = Intent(context, SingleOrderActivity::class.java)
                val bundle = Bundle().apply {
                    putInt(SingleOrderActivity.ORDER_ID_KEY, orders[absoluteAdapterPosition].id)
                }
                intent.putExtras(bundle)
                context.startActivity(intent)
            }

            reorderButton.setOnClickListener {
                doIfConnectedToInternet {
                    viewModel.reorderByOrderId(orders[absoluteAdapterPosition].id, UserCredentials.getToken()!!)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_order_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(orders[position]){
                orderDate.text = this.orderPlacedDate.toString()
                orderSummary.text = this.orderDetails[0].menu.name
            }
        }
    }

    fun setData(newData: List<Order<User>>){
        orders = newData
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    fun doIfConnectedToInternet(task: () -> Unit){
        val networkHelper = NetworkHelper(context)
        if(networkHelper.isConnectedToNetwork()){
            task()
        }else{
            val alertManager = AlertManager(context)
            alertManager.showAlertWithOkButton(AlertData(
                title = "No internet",
                message = "please connect to the internet"
            ))
        }
    }
}