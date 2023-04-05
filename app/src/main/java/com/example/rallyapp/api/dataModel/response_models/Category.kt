package com.example.rallyapp.api.dataModel.response_models

data class Category(
    val category: String,
    val id: Int,
    var selected: Boolean = false
)