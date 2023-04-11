package com.example.rallyapp.api.dataModel.request_models

data class ApplyVoucherToOrderRequest(
    var orderId: Int,
    var voucherCode: String
)
