package com.example.rallyapp.api.dataModel.response_models

data class Voucher(
    val code: String,
    val expiration: String,
    val id: Int,
    val offerPercent: Int
)