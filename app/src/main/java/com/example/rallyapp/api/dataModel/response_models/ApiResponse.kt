package com.example.rallyapp.api.dataModel.response_models

data class ApiResponse<T>(
    val success: Int,
    val message: String,
    val data: List<T>
)
