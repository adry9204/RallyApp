package com.example.rallyapp.recyclerview_adpaters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rallyapp.R
import com.example.rallyapp.activities.SingleOrderActivity
import com.example.rallyapp.api.dataModel.response_models.Order
import com.example.rallyapp.api.dataModel.response_models.OrderDetail
import com.example.rallyapp.api.dataModel.response_models.OrdersHelper
import com.example.rallyapp.api.dataModel.response_models.User
import com.example.rallyapp.fragments.OrderReceiptBottomSheetFragment
import com.example.rallyapp.user.UserCredentials
import com.example.rallyapp.utils.AlertData
import com.example.rallyapp.utils.AlertManager
import com.example.rallyapp.utils.NetworkHelper
import com.example.rallyapp.viewModel.OrderActivityViewModel
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class OrdersAdapter (
    private val context: Context,
    private var orders: List<Order<User>>,
    private val viewModel: OrderActivityViewModel,
    private val supportFragmentManager: FragmentManager,
    private val showLoadingScreen: () -> Unit
) : RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderDate: TextView = itemView.findViewById(R.id.date_and_total_label)
        val orderSummary: TextView = itemView.findViewById(R.id.order_description_label)
        private val reorderButton: Button = itemView.findViewById(R.id.reorder_button)
        private val viewReceiptButton: Button = itemView.findViewById(R.id.view_receipt_button)
        val orderStatus: TextView = itemView.findViewById(R.id.order_status_label)

        init {
            itemView.setOnClickListener{
                val intent = Intent(context, SingleOrderActivity::class.java)
                val bundle = Bundle().apply {
                    putInt(SingleOrderActivity.ORDER_ID_KEY, orders[absoluteAdapterPosition].id)
                }
                intent.putExtras(bundle)
                context.startActivity(intent)
            }

            viewReceiptButton.setOnClickListener {
                val orderReceiptBottomSheetFragment = OrderReceiptBottomSheetFragment(order = orders[absoluteAdapterPosition])
                orderReceiptBottomSheetFragment.show(supportFragmentManager, orderReceiptBottomSheetFragment.tag)
            }

            reorderButton.setOnClickListener {
                doIfConnectedToInternet {
                    showLoadingScreen()
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
                orderDate.text = makeDatePriceString(this)
                CoroutineScope(Dispatchers.Default).launch{
                    makeDescriptionFromOrderDetails(orderDetails, orderSummary)
                }
                orderStatus.text = getOrderStatusString(this)
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

    private suspend fun makeDescriptionFromOrderDetails(orderDetails: List<OrderDetail>, textView: TextView){
        coroutineScope {
            var finalString = ""
            var orderDetailCount = 0
            for (orderDetail in orderDetails){
                orderDetailCount++
                finalString = finalString.plus("${orderDetail.menu.name},")
                if(finalString.length > 20){
                    finalString = finalString.slice(0..20)
                    break
                }
            }
            if(orderDetailCount < orderDetails.size){
                finalString = finalString.plus("...+ ${orderDetails.size - orderDetailCount}items")
            }else{
                finalString = finalString.slice(0..finalString.length-2)
            }
            withContext(Dispatchers.Main){
                textView.text = finalString
            }
        }
    }

    private fun makeDatePriceString(order: Order<User>): String{
        val dateString = formatDate(order.orderPlacedDate!!)
        val price = if (order.afterOfferPrice != null) {order.afterOfferPrice} else {order.totalPrice}
        return "$dateString - $$price"
    }

    private fun formatDate(date: Date): String {
        val format = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        return format.format(date)
    }

    private fun getOrderStatusString(order: Order<User>): String{
        return "status: ${OrdersHelper.getStatusFromOrder(order)}"
    }
}