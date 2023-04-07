package com.example.rallyapp.api.dataModel.response_models

data class OrderDetail(
    val id: Int,
    val menu: Menu,
    val price: String,
    val quantity: Int
)