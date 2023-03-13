package com.example.rallyapp.network

import com.example.rallyapp.dataModel.*
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header

interface UserService {

        @POST("users/register")
        fun registerUser(@Body user: RegisterRequest): Call<RegisterResponse>

        @POST("users/login")
        fun loginUser(@Body user: LoginRequest): Call<LoginResponse>

        @DELETE("users/logout")
        fun logoutUser(@Header("Authorization") token: String): Call<LogoutResponse>
}