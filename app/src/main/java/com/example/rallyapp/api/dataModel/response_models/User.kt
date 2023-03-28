package com.example.rallyapp.api.dataModel.response_models

data class User(
    val email: String ?= null,
    val fullName: String ?= null,
    val id: Int,
    val password: String ?= null,
    val userName: String ?= null,
    val verified: Boolean ?= null,
)