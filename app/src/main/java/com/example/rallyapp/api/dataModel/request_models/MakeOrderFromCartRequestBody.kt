package com.example.rallyapp.api.dataModel.request_models

data class MakeOrderFromCartRequestBody(
    var userId: Int,
    var voucherId: Int? = null
)
