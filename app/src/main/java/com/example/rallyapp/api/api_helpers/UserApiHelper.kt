package com.example.rallyapp.api.api_helpers

import android.util.Log
import com.example.rallyapp.activities.LoginActivity
import com.example.rallyapp.api.dataModel.request_models.UpdateUserRequest
import com.example.rallyapp.api.dataModel.response_models.ApiResponse
import com.example.rallyapp.api.dataModel.response_models.Menu
import com.example.rallyapp.api.dataModel.response_models.User
import com.example.rallyapp.api.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserApiHelper{
    fun getUserById(
        userId: Int,
        token: String,
        callback: (response: ApiResponse<User>) -> Unit
    ){
        val retrofit = RetrofitClient.userRetrofit.getUserById(userId, token)
        retrofit.enqueue(object : Callback<ApiResponse<User>> {
            override fun onResponse(
                call: Call<ApiResponse<User>>,
                response: Response<ApiResponse<User>>
            ) {
                if(response.body() != null){
                    val result = response.body()!!
                    callback(result)
                }
            }

            override fun onFailure(call: Call<ApiResponse<User>>, t: Throwable) {
                callback(ApiResponse(
                    success = 0,
                    message = "Failed api call",
                    data = listOf()
                ))
            }

        })
    }

    fun updateUserById(
        user: UpdateUserRequest,
        token: String,
        callback: (response: ApiResponse<User>) -> Unit
    ){
        val retrofit = RetrofitClient.userRetrofit.updateUser(user, token)
        retrofit.enqueue(object : Callback<ApiResponse<User>> {
            override fun onResponse(
                call: Call<ApiResponse<User>>,
                response: Response<ApiResponse<User>>
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
            override fun onFailure(call: Call<ApiResponse<User>>, t: Throwable) {
                callback(ApiResponse(
                    success = 0,
                    message = "Failed api call",
                    data = listOf()
                ))
            }
        })
    }
}