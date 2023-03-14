package com.example.rallyapp.dataModel

data class ApiResponse<T>(
    val success: Int,
    val message: String,
    val data: List<T>
)
