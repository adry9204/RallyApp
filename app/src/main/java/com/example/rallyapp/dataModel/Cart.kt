package com.example.rallyapp.dataModel

data class Cart(
    val id: Int,
    val menu: Menu,
    val price: String,
    val quantity: Int,
    val user: User
)