package com.example.rallyapp.dataModel.response_models

data class ApiResponse<T>(
    val success: Int,
    val message: String,
    val data: List<T>
)
