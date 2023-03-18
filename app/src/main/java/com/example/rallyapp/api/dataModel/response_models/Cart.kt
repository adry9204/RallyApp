package com.example.rallyapp.api.dataModel.response_models

data class Cart(
    val id: Int,
    val menu: Menu,
    val price: String,
    val quantity: Int,
    val user: User
)