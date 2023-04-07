package com.example.rallyapp.api.dataModel.request_models

data class MakePaymentRequestBody(
    val addressId: Int? = null,
    val orderId: Int
)