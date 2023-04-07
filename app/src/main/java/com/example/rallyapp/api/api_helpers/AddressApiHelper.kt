package com.example.rallyapp.api.api_helpers

import android.util.Log
import com.example.rallyapp.activities.LoginActivity
import com.example.rallyapp.api.dataModel.response_models.Address
import com.example.rallyapp.api.dataModel.response_models.ApiResponse
import com.example.rallyapp.api.dataModel.response_models.Cart
import com.example.rallyapp.api.dataModel.response_models.User
import com.example.rallyapp.api.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddressApiHelper {

    fun getUsersAddress(userId: Int, authorizationToken: String, callback: (ApiResponse<Address<User>>) -> Unit){
        val retrofit = RetrofitClient.addressClient.getUsersAddress(userId, authorizationToken)
        retrofit.enqueue(object : Callback<ApiResponse<Address<User>>> {
            override fun onResponse(
                call: Call<ApiResponse<Address<User>>>,
                response: Response<ApiResponse<Address<User>>>
            ) {
                if(response.body() != null){
                    val result = response.body()!!
                    callback(result)
                }else{
                    callback(ApiResponse(
                        success = 0,
                        message = "Failed to get users address",
                        data = listOf()
                    ))
                }
            }
            override fun onFailure(call: Call<ApiResponse<Address<User>>>, t: Throwable) {
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