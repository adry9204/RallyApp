package com.example.rallyapp.api.dataModel.response_models

data class Menu(
    val category: Category,
    val description: String,
    val id: Int,
    val image: String,
    val name: String,
    val price: String
)