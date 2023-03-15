package com.example.rallyapp.repo

import android.content.Context
import android.util.Log
import com.example.rallyapp.LoginActivity
import com.example.rallyapp.dataModel.request_models.AddCartRequestBody
import com.example.rallyapp.dataModel.response_models.ApiResponse
import com.example.rallyapp.dataModel.response_models.Cart
import com.example.rallyapp.network.RetrofitClient
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartRepo(context: Context) {

    fun getUsersCart(userId: Int, authorizationToken: String, callback: (List<Cart>) -> Unit){
        val retrofit = RetrofitClient.cartClient.getUsersCart(userId, authorizationToken)
        retrofit.enqueue(object : Callback<ApiResponse<Cart>> {
            override fun onResponse(
                call: Call<ApiResponse<Cart>>,
                response: Response<ApiResponse<Cart>>
            ) {
                if(response.body() != null){
                    val result = response.body()!!.data
                    callback(result)
                }else{
                    val result = listOf<Cart>()
                    callback(result)
                }
            }
            override fun onFailure(call: Call<ApiResponse<Cart>>, t: Throwable) {
                Log.e(LoginActivity.TAG, "Api register call failed message: " + t.message)
            }
        })
    }

    fun addCItemToCart(
        userId: Int,
        menuId: Int,
        quantity: Int,
        authorizationToken: String,
        callback: (ApiResponse<Cart>) -> Unit)
    {
        val requestBody = AddCartRequestBody(
            menuId = menuId,
            quantity = quantity,
            userId = userId
        )

        val retrofit = RetrofitClient.cartClient.addCartItem(requestBody, authorizationToken)
        retrofit.enqueue(object : Callback<ApiResponse<Cart>>{
            override fun onResponse(
                call: Call<ApiResponse<Cart>>,
                response: Response<ApiResponse<Cart>>
            ) = if(response.body() != null){
                val result = response.body()!!
                callback(result)
            }else{
                val json = response.errorBody()!!.toString()
                var gson = Gson()
                val result = gson.fromJson<ApiResponse<Cart>>(
                    json,
                    object : TypeToken<ApiResponse<Cart>>() {}.type
                )
                callback(result)
            }

            override fun onFailure(call: Call<ApiResponse<Cart>>, t: Throwable) {
                Log.e(LoginActivity.TAG, "Api register call failed message: " + t.message)
            }
        })

    }

}