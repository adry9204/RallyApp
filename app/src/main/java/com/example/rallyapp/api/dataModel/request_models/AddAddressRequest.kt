package com.example.rallyapp.api.dataModel.request_models

data class AddAddressRequest(
    val city: String,
    val country: String,
    val line1: String,
    val line2: String?,
    val name: String,
    val postalCode: String,
    val province: String,
    val userId: Int
)