package com.example.rallyapp.api.dataModel.response_models

data class Order(
    val beforeTaxPrice: String,
    val id: Int,
    val orderDetails: List<OrderDetail>,
    val paid: Boolean,
    val status: String,
    val stripePaymentIntent: Any,
    val taxPrice: String,
    val totalPrice: String,
    val user: User
)