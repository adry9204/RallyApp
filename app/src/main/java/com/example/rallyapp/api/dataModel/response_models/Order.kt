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
    var orderPlacedDate: Date? = null
): Serializable