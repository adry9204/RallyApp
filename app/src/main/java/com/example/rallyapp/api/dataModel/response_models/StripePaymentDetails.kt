package com.example.rallyapp.api.dataModel.response_models

data class StripePaymentDetails(
    val customer: String,
    val ephemeralKey: String,
    val orderId: Int,
    val paymentIntent: String,
    val publishableKey: String
)