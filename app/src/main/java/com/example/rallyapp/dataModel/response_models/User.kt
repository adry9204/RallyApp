package com.example.rallyapp.dataModel.response_models

data class User(
    val email: String,
    val fullName: String,
    val id: Int,
    val password: String,
    val userName: String,
    val verified: Boolean
)