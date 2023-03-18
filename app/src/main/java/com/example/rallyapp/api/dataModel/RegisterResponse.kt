package com.example.rallyapp.api.dataModel

data class RegisterResponse(
    val `data`: List<Data>,
    val message: String,
    val success: Int
)