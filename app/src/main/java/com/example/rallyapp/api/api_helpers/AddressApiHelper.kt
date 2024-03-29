package com.example.rallyapp.api.api_helpers

import android.util.Log
import com.example.rallyapp.activities.LoginActivity
import com.example.rallyapp.api.dataModel.request_models.AddAddressRequest
import com.example.rallyapp.api.dataModel.response_models.Address
import com.example.rallyapp.api.dataModel.response_models.ApiResponse
import com.example.rallyapp.api.dataModel.response_models.Cart
import com.example.rallyapp.api.dataModel.response_models.User
import com.example.rallyapp.api.network.RetrofitClient
import org.json.JSONObject
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
                    val errorBodyString = response.errorBody()?.string() ?: ""
                    val errorMessage = JSONObject(errorBodyString).getString("message")
                    callback(ApiResponse(
                        success = 0,
                        message = errorMessage,
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

    fun addNewAddress(
        addAddressRequest: AddAddressRequest,
        authorizationToken: String,
        callback: (ApiResponse<Address<Int>>) -> Unit
    ){
        val retrofit = RetrofitClient.addressClient.addNewAddress(addAddressRequest, authorizationToken)
        retrofit.enqueue(object : Callback<ApiResponse<Address<Int>>> {
            override fun onResponse(
                call: Call<ApiResponse<Address<Int>>>,
                response: Response<ApiResponse<Address<Int>>>
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
            override fun onFailure(call: Call<ApiResponse<Address<Int>>>, t: Throwable) {
                Log.e(LoginActivity.TAG, "Api register call failed message: " + t.message)
                callback(ApiResponse(
                    success = 0,
                    message = t.message.toString(),
                    data = listOf()
                ))
            }
        })
    }

    fun deleteAddress(
        addressId: Int,
        token: String,
        callback: (ApiResponse<Address<Int>>) -> Unit
    ){
        val retrofit = RetrofitClient.addressClient.deleteAddress(addressId, token)
        retrofit.enqueue(object : Callback<ApiResponse<Address<Int>>> {
            override fun onResponse(
                call: Call<ApiResponse<Address<Int>>>,
                response: Response<ApiResponse<Address<Int>>>
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
            override fun onFailure(call: Call<ApiResponse<Address<Int>>>, t: Throwable) {
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