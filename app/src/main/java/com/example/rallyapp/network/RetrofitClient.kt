package com.example.rallyapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    val registerRetrofit: UserService = Retrofit.Builder()
        .baseUrl("http://192.168.2.57:8000/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(UserService::class.java)

    val loginRetrofit: UserService = Retrofit.Builder()
        .baseUrl("http://192.168.2.57:8000/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(UserService::class.java)

}