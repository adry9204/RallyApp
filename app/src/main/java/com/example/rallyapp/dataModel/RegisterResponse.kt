package com.example.rallyapp.dataModel

data class RegisterResponse(
    val `data`: List<Data>,
    val message: String,
    val success: Int
)