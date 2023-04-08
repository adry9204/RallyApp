package com.example.rallyapp.api.api_helpers

import android.util.Log
import com.example.rallyapp.activities.LoginActivity
import com.example.rallyapp.api.dataModel.request_models.MakeOrderFromCartRequestBody
import com.example.rallyapp.api.dataModel.request_models.MakePaymentRequestBody
import com.example.rallyapp.api.dataModel.response_models.*
import com.example.rallyapp.api.network.RetrofitClient
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class OrderApiHelper {

    fun makeOrderFormCart(
        userId: Int,
        token: String,
        callback: (response: ApiResponse<Order<Int>>) -> Unit
    ){
        val makeOrderFromCartRequestBody = MakeOrderFromCartRequestBody(userId=userId)
        val retrofit = RetrofitClient.orderClient.makeOrderFromUsersCart(makeOrderFromCartRequestBody, token)
        retrofit.enqueue(object : retrofit2.Callback<ApiResponse<Order<Int>>> {
            override fun onResponse(
                call: Call<ApiResponse<Order<Int>>>,
                response: Response<ApiResponse<Order<Int>>>
            ) {
                if(response.body() != null){
                    val result = response.body()!!
                    callback(result)
                }else{
                    callback(ApiResponse(
                        success = 0,
                        message = "Failed to make order",
                        data = listOf()
                    ))
                }
            }

            override fun onFailure(call: Call<ApiResponse<Order<Int>>>, t: Throwable) {
                Log.e(LoginActivity.TAG, "Api register call failed message: " + t.message)
                callback(ApiResponse(
                    success = 0,
                    message = t.message.toString(),
                    data = listOf()
                ))
            }

        })
    }

    fun getOrderById(
        orderId: Int,
        token: String,
        callback: (response: ApiResponse<Order<User>>) -> Unit
    ){
        val retrofit = RetrofitClient.orderClient.getOrderById(orderId, token)
        retrofit.enqueue(object : retrofit2.Callback<ApiResponse<Order<User>>> {
            override fun onResponse(
                call: Call<ApiResponse<Order<User>>>,
                response: Response<ApiResponse<Order<User>>>
            ) {
                if(response.body() != null){
                    val result = response.body()!!
                    callback(result)
                }else{
                    callback(ApiResponse(
                        success = 0,
                        message = "Failed to make order",
                        data = listOf()
                    ))
                }
            }

            override fun onFailure(call: Call<ApiResponse<Order<User>>>, t: Throwable) {
                Log.e(LoginActivity.TAG, "Api register call failed message: " + t.message)
                callback(ApiResponse(
                    success = 0,
                    message = t.message.toString(),
                    data = listOf()
                ))
            }

        })
    }

    fun getUsersOrder(
        userId: Int,
        token: String,
        callback: (response: ApiResponse<Order<User>>) -> Unit
    ){
        val retrofit = RetrofitClient.orderClient.getUsersOrders(userId, token)
        retrofit.enqueue(object : retrofit2.Callback<ApiResponse<Order<User>>> {
            override fun onResponse(
                call: Call<ApiResponse<Order<User>>>,
                response: Response<ApiResponse<Order<User>>>
            ) {
                if(response.body() != null){
                    val result = response.body()!!
                    callback(result)
                }else{
                    callback(ApiResponse(
                        success = 0,
                        message = "Failed to make order",
                        data = listOf()
                    ))
                }
            }

            override fun onFailure(call: Call<ApiResponse<Order<User>>>, t: Throwable) {
                Log.e(LoginActivity.TAG, "Api register call failed message: " + t.message)
                callback(ApiResponse(
                    success = 0,
                    message = t.message.toString(),
                    data = listOf()
                ))
            }

        })
    }

    fun makePaymentIntent(
        orderId: Int,
        addressId: Int,
        token: String,
        callback: (response: ApiResponse<StripePaymentDetails>) -> Unit
    ) {
        val makePaymentRequestBody = MakePaymentRequestBody(orderId = orderId, addressId = addressId)
        val retrofit = RetrofitClient.orderClient.makePaymentIntent(makePaymentRequestBody, token)
        retrofit.enqueue(object : retrofit2.Callback<ApiResponse<StripePaymentDetails>> {
            override fun onResponse(
                call: Call<ApiResponse<StripePaymentDetails>>,
                response: Response<ApiResponse<StripePaymentDetails>>
            ) {
                if(response.body() != null){
                    val result = response.body()!!
                    callback(result)
                }else{
                    val errorBodyString = response.errorBody()?.string() ?: ""
                    val errorMessage = JSONObject(errorBodyString).getString("message")
                    callback(ApiResponse(
                        success = 0,
                        message = errorMessage,
                        data = listOf()
                    ))
                }
            }

            override fun onFailure(call: Call<ApiResponse<StripePaymentDetails>>, t: Throwable) {
                Log.e(LoginActivity.TAG, "Api register call failed message: " + t.message)
                callback(ApiResponse(
                    success = 0,
                    message = t.message.toString(),
                    data = listOf()
                ))
            }
        })
    }

    fun confirmOrder(
        orderId: Int,
        token: String,
        callback: (response: ApiResponse<Order<Int>>) -> Unit
    ) {
        val makePaymentRequestBody = MakePaymentRequestBody(orderId = orderId)
        val retrofit = RetrofitClient.orderClient.confirmPaid(makePaymentRequestBody, token)
        retrofit.enqueue(object : retrofit2.Callback<ApiResponse<Order<Int>>> {
            override fun onResponse(
                call: Call<ApiResponse<Order<Int>>>,
                response: Response<ApiResponse<Order<Int>>>
            ) {
                if(response.body() != null){
                    val result = response.body()!!
                    callback(result)
                }else{
                    val errorBodyString = response.errorBody()?.string() ?: ""
                    val errorMessage = JSONObject(errorBodyString).getString("message")
                    callback(ApiResponse(
                        success = 0,
                        message = errorMessage,
                        data = listOf()
                    ))
                }
            }

            override fun onFailure(call: Call<ApiResponse<Order<Int>>>, t: Throwable) {
                Log.e(LoginActivity.TAG, "Api register call failed message: " + t.message)
                callback(ApiResponse(
                    success = 0,
                    message = t.message.toString(),
                    data = listOf()
                ))
            }
        })
    }

    fun deleteOrder(
        orderId: Int,
        token: String,
        callback: (response: ApiResponse<Order<Int>>) -> Unit
    ){
        val retrofit = RetrofitClient.orderClient.deleteOrder(orderId, token)
        retrofit.enqueue(object : retrofit2.Callback<ApiResponse<Order<Int>>> {
            override fun onResponse(
                call: Call<ApiResponse<Order<Int>>>,
                response: Response<ApiResponse<Order<Int>>>
            ) {
                if(response.body() != null){
                    val result = response.body()!!
                    callback(result)
                }else{
                    callback(ApiResponse(
                        success = 0,
                        message = "Failed to delete order",
                        data = listOf()
                    ))
                }
            }

            override fun onFailure(call: Call<ApiResponse<Order<Int>>>, t: Throwable) {
                Log.e(LoginActivity.TAG, "Api register call failed message: " + t.message)
                callback(ApiResponse(
                    success = 0,
                    message = t.message.toString(),
                    data = listOf()
                ))
            }
        })
    }

    fun reorderByOrderId(
        orderId: Int,
        token: String,
        callback: (response: ApiResponse<Order<User>>) -> Unit
    ){
        val makePaymentRequestBody = MakePaymentRequestBody(orderId = orderId)
        val retrofit = RetrofitClient.orderClient.reorderByOrderId(makePaymentRequestBody, token)
        retrofit.enqueue(object : retrofit2.Callback<ApiResponse<Order<User>>> {
            override fun onResponse(
                call: Call<ApiResponse<Order<User>>>,
                response: Response<ApiResponse<Order<User>>>
            ) {
                if(response.body() != null){
                    val result = response.body()!!
                    callback(result)
                }else{
                    callback(ApiResponse(
                        success = 0,
                        message = "Failed to find order",
                        data = listOf()
                    ))
                }
            }

            override fun onFailure(call: Call<ApiResponse<Order<User>>>, t: Throwable) {
                Log.e(LoginActivity.TAG, "Api register call failed message: " + t.message)
                callback(ApiResponse(
                    success = 0,
                    message = t.message.toString(),
                    data = listOf()
                ))
            }
        })
    }


}