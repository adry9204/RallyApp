package com.example.rallyapp.dataModel

data class LoginResponse(
    val `data`: List<DataInfo>,
    val message: String,
    val success: Int
)