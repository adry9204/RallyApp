package com.example.rallyapp.api.dataModel.response_models


import java.io.Serializable

data class Order<UserType>(
    val beforeTaxPrice: String,
    val id: Int,
    val orderDetails: List<OrderDetail>,
    val paid: Boolean,
    val status: String,
    val stripePaymentIntent: String?,
    val taxPrice: String,
    val totalPrice: String,
    val user: UserType
): Serializable