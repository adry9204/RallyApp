package com.example.rallyapp.api.dataModel.response_models


import java.io.Serializable
import java.util.Date

data class Order<UserType>(
    val beforeTaxPrice: String,
    val id: Int,
    val orderDetails: List<OrderDetail>,
    val paid: Boolean,
    val status: String,
    val stripePaymentIntent: String?,
    val taxPrice: String,
    val totalPrice: String,
    val user: UserType,
    var afterOfferPrice: String? = null,
    var orderPlacedDate: Date? = null,
    var voucher: Voucher? = null,
    var orderMethod: String? = null
)

object OrdersHelper{

    const val pending = "pending"
    const val rejected = "rejected"
    const val preparing = "preparing"
    const val waiting_for_pickup = "waiting_for_pickup"
    const val on_the_way = "on_the_way"
    const val completed = "completed"

    fun getStatusFromOrder(order: Order<*>): String{
        if(order.orderMethod == "delivery"){
            return when(order.status){
                pending -> "pending"
                rejected -> "rejected"
                preparing -> "preparing"
                waiting_for_pickup  -> "waiting for pickup"
                on_the_way -> "on the way"
                completed -> "completed"
                else -> order.status
            }
        }else{
            return when(order.status){
                pending -> "pending"
                rejected -> "rejected"
                preparing -> "preparing"
                waiting_for_pickup  -> "ready for pickup"
                completed -> "completed"
                else -> order.status
            }
        }

    }
}