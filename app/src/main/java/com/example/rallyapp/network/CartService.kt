package com.example.rallyapp.network

import com.example.rallyapp.dataModel.request_models.AddCartRequestBody
import com.example.rallyapp.dataModel.response_models.ApiResponse
import com.example.rallyapp.dataModel.response_models.Cart
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface CartService {

    companion object {
        const val USER_ID = "user_id"
    }

    @GET("cart/user/{$USER_ID}")
    fun getUsersCart(@Path(USER_ID) userId: Int, @Header("Authorization") token: String): Call<ApiResponse<Cart>>

    @POST("cart/")
    fun addCartItem(@Body addCartRequestBody: AddCartRequestBody, @Header("Authorization") token: String): Call<ApiResponse<Cart>>
}