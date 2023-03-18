package com.example.rallyapp.api.dataModel

data class LogoutResponse(
    val `data`: List<LogoutInfo>,
    val message: String,
    val success: Int
)