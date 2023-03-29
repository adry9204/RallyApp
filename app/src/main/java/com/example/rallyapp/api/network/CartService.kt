package com.example.rallyapp.api.network

import com.example.rallyapp.api.dataModel.request_models.AddCartRequestBody
import com.example.rallyapp.api.dataModel.request_models.UpdateCartQuantityBody
import com.example.rallyapp.api.dataModel.response_models.ApiResponse
import com.example.rallyapp.api.dataModel.response_models.Cart
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface CartService {

    companion object {
        const val USER_ID = "user_id"
        const val CART_ID = "cart_id"

        const val AUTH_HEADER = "Authorization"
    }

    @GET("cart/user/{$USER_ID}")
    fun getUsersCart(@Path(USER_ID) userId: Int, @Header(AUTH_HEADER) token: String): Call<ApiResponse<Cart>>

    @POST("cart/")
    fun addCartItem(@Body addCartRequestBody: AddCartRequestBody, @Header(AUTH_HEADER) token: String): Call<ApiResponse<Cart>>

    @DELETE("cart/{$CART_ID}")
    fun removeFromCart(@Path(CART_ID) cartId: Int, @Header(AUTH_HEADER) token: String): Call<ApiResponse<Cart>>

    @PATCH("cart/quantity")
    fun updateCartQuantity(@Body updateCartQuantityBody: UpdateCartQuantityBody, @Header(AUTH_HEADER) token: String): Call<ApiResponse<Cart>>

}