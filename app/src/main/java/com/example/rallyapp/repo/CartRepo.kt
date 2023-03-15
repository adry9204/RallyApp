package com.example.rallyapp.repo

import android.content.Context
import android.util.Log
import com.example.rallyapp.LoginActivity
import com.example.rallyapp.dataModel.ApiResponse
import com.example.rallyapp.dataModel.Cart
import com.example.rallyapp.dataModel.Menu
import com.example.rallyapp.network.RetrofitClient
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

}