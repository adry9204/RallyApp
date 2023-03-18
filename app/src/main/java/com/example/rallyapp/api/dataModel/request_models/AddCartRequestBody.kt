package com.example.rallyapp.api.dataModel.request_models

data class AddCartRequestBody(
    val menuId: Int,
    val quantity: Int,
    val userId: Int
)