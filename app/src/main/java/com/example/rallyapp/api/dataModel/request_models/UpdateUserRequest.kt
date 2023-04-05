package com.example.rallyapp.api.dataModel.request_models

data class UpdateUserRequest(
    val email: String,
    val fullName: String,
    val password: String? = null,
    val userId: Int,
    val userName: String
)