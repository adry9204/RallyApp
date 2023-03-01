package com.example.rallyapp.network

import com.example.rallyapp.dataModel.LoginRequest
import com.example.rallyapp.dataModel.LoginResponse
import com.example.rallyapp.dataModel.RegisterRequest
import com.example.rallyapp.dataModel.RegisterResponse
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Body

interface UserService {

        @POST("users/register")
        fun registerUser(@Body user: RegisterRequest): Call<RegisterResponse>

        @POST("users/login")
        fun loginUser(@Body user: LoginRequest): Call<LoginResponse>
}