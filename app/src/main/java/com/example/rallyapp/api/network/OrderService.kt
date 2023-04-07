package com.example.rallyapp.api.network

import com.example.rallyapp.api.dataModel.request_models.AddCartRequestBody
import com.example.rallyapp.api.dataModel.request_models.MakeOrderFromCartRequestBody
import com.example.rallyapp.api.dataModel.response_models.ApiResponse
import com.example.rallyapp.api.dataModel.response_models.Menu
import com.example.rallyapp.api.dataModel.response_models.Order
import com.example.rallyapp.api.dataModel.response_models.User
import retrofit2.Call
import retrofit2.http.*

interface OrderService {
    companion object{
        const val ORDER_ID = "order_id"
    }

    @POST("orders")
    fun makeOrderFromUsersCart(
        @Body makeOrderFromCartRequestBody: MakeOrderFromCartRequestBody,
        @Header(CartService.AUTH_HEADER) token: String
    ): Call<ApiResponse<Order<Int>>>

    @GET("orders/{${ORDER_ID}}")
    fun getOrderById(@Path(ORDER_ID) orderId: Int,  @Header(CartService.AUTH_HEADER) token: String): Call<ApiResponse<Order<User>>>

}