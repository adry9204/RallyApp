package com.example.rallyapp.api.network

import com.example.rallyapp.api.dataModel.request_models.MakeOrderFromCartRequestBody
import com.example.rallyapp.api.dataModel.request_models.MakePaymentRequestBody
import com.example.rallyapp.api.dataModel.response_models.ApiResponse
import com.example.rallyapp.api.dataModel.response_models.Order
import com.example.rallyapp.api.dataModel.response_models.StripePaymentDetails
import com.example.rallyapp.api.dataModel.response_models.User
import retrofit2.Call
import retrofit2.http.*

interface OrderService {
    companion object{
        const val ORDER_ID = "order_id"
        const val USER_ID  = "user_Id"
    }

    @POST("orders")
    fun makeOrderFromUsersCart(
        @Body makeOrderFromCartRequestBody: MakeOrderFromCartRequestBody,
        @Header(CartService.AUTH_HEADER) token: String
    ): Call<ApiResponse<Order<Int>>>

    @GET("orders/{${ORDER_ID}}")
    fun getOrderById(
        @Path(ORDER_ID) orderId: Int,
        @Header(CartService.AUTH_HEADER) token: String
    ): Call<ApiResponse<Order<User>>>

    @POST("orders/makepayment")
    fun makePaymentIntent(
        @Body makePaymentRequestBody: MakePaymentRequestBody,
        @Header(CartService.AUTH_HEADER) token: String
    ): Call<ApiResponse<StripePaymentDetails>>

    @POST("orders/placeorder")
    fun confirmPaid(
        @Body makePaymentRequestBody: MakePaymentRequestBody,
        @Header(CartService.AUTH_HEADER) token: String
    ): Call<ApiResponse<Order<Int>>>

    @DELETE("orders/{$ORDER_ID}")
    fun deleteOrder(
        @Path(ORDER_ID) orderId: Int,
        @Header(CartService.AUTH_HEADER) token: String
    ): Call<ApiResponse<Order<Int>>>

    @GET("orders/user/{$USER_ID}")
    fun getUsersOrders(
        @Path(USER_ID) userId: Int,
        @Header(CartService.AUTH_HEADER) token: String
    ): Call<ApiResponse<Order<User>>>

    @POST("orders/reorder")
    fun reorderByOrderId(
        @Body makePaymentRequestBody: MakePaymentRequestBody,
        @Header(CartService.AUTH_HEADER) token: String
    ): Call<ApiResponse<Order<User>>>

}