package com.example.rallyapp.repo

import com.example.rallyapp.api.api_helpers.OrderApiHelper
import com.example.rallyapp.api.dataModel.response_models.ApiResponse
import com.example.rallyapp.api.dataModel.response_models.Order
import com.example.rallyapp.api.dataModel.response_models.User

class OrderRepo {

    private val orderApiHelper: OrderApiHelper = OrderApiHelper()

    fun makeOrderFromCart(userId: Int, token: String, callback: (response: ApiResponse<Order<Int>>) -> Unit){
        orderApiHelper.makeOrderFormCart(userId, token){
            callback(it)
        }
    }

    fun getOrderById(orderId: Int, token: String, callback:(response: ApiResponse<Order<User>>) -> Unit){
        orderApiHelper.getOrderById(orderId, token){
            callback(it)
        }
    }
}