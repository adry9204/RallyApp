package com.example.rallyapp.api.dataModel.response_models

data class Cart(
    val id: Int,
    val menu: Menu,
    val price: String,
    var quantity: Int,
    val user: User,
    var pending: Boolean = false
)