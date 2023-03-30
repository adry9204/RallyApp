package com.example.rallyapp.api.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val server = "https://still-brushlands-66800.herokuapp.com"

    val userRetrofit: UserService = Retrofit.Builder()
        .baseUrl("$server/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(UserService::class.java)

    val menuClient: MenuService = Retrofit.Builder()
        .baseUrl("$server/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(MenuService::class.java)

    val cartClient: CartService = Retrofit.Builder()
        .baseUrl("$server/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CartService::class.java)

    val categoryClient: CategoryService = Retrofit.Builder()
        .baseUrl("$server/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CategoryService::class.java)
}