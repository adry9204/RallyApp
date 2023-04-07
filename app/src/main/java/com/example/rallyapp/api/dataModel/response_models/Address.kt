package com.example.rallyapp.api.dataModel.response_models

data class Address<UserType>(
    val id: Int? = null,
    val country: String,
    val line1: String,
    var line2: String? = null,
    val name: String,
    val postalCode: String,
    val province: String,
    val userId: UserType,
    var expanded: Boolean = false,
    var selected: Boolean = false
)