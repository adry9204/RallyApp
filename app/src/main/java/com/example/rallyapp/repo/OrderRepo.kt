package com.example.rallyapp.repo

import com.example.rallyapp.api.api_helpers.OrderApiHelper
import com.example.rallyapp.api.dataModel.response_models.ApiResponse
import com.example.rallyapp.api.dataModel.response_models.Order
import com.example.rallyapp.api.dataModel.response_models.StripePaymentDetails
import com.example.rallyapp.api.dataModel.response_models.User
import retrofit2.Call

class OrderRepo {

    private val orderApiHelper: OrderApiHelper = OrderApiHelper()

    fun makeOrderFromCart(
        userId: Int,
        token: String,
        callback: (response: ApiResponse<Order<Int>>) -> Unit
    ){
        orderApiHelper.makeOrderFormCart(userId, token){
            callback(it)
        }
    }

    fun getOrderById(
        orderId: Int,
        token: String,
        callback:(response: ApiResponse<Order<User>>) -> Unit
    ){
        orderApiHelper.getOrderById(orderId, token){
            callback(it)
        }
    }

    fun makePaymentIntent(
        orderId: Int,
        addressId: Int,
        token: String,
        callback: (response: ApiResponse<StripePaymentDetails>) -> Unit
    ){
        orderApiHelper.makePaymentIntent(orderId, addressId, token){
            callback(it)
        }
    }

    fun confirmOrder(
        orderId: Int,
        token: String,
        callback:(response: ApiResponse<Order<Int>>) -> Unit
    ){
        orderApiHelper.confirmOrder(orderId, token){
            callback(it)
        }
    }

    fun deleteOrder(
        orderId: Int,
        token: String,
        callback:(response: ApiResponse<Order<Int>>) -> Unit
    ){
        orderApiHelper.deleteOrder(orderId, token){
            callback(it)
        }
    }

    fun getUsersOrder(
        userId: Int,
        token: String,
        callback:(response: ApiResponse<Order<User>>) -> Unit
    ){
        orderApiHelper.getUsersOrder(userId, token){
            callback(it)
        }
    }

    fun reorderById(
        orderId: Int,
        token: String,
        callback:(response: ApiResponse<Order<User>>) -> Unit
    ){
        orderApiHelper.reorderByOrderId(orderId, token){
            callback(it)
        }
    }
}