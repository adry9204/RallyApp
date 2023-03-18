package com.example.rallyapp.api.dataModel

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("userName") val userName: String,
    @SerializedName("password") val password: String
)
