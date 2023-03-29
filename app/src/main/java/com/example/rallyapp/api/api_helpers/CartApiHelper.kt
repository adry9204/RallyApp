package com.example.rallyapp.api.api_helpers

import android.util.Log
import com.example.rallyapp.activities.LoginActivity
import com.example.rallyapp.api.dataModel.request_models.AddCartRequestBody
import com.example.rallyapp.api.dataModel.request_models.UpdateCartQuantityBody
import com.example.rallyapp.api.dataModel.response_models.ApiResponse
import com.example.rallyapp.api.dataModel.response_models.Cart
import com.example.rallyapp.api.network.RetrofitClient
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartApiHelper {
    companion object{
        const val TAG = "CartApiHelper"
    }

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

    fun addItemToCart(
        userId: Int,
        menuId: Int,
        quantity: Int,
        authorizationToken: String,
        callback: (ApiResponse<Cart>) -> Unit
    ){
        val requestBody = AddCartRequestBody(
            menuId = menuId,
            quantity = quantity,
            userId = userId
        )
        Log.i(TAG, authorizationToken)
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

    fun removeFromCart(cartId: Int, token: String, callback: (ApiResponse<Cart>) -> Unit){
        val retrofit = RetrofitClient.cartClient.removeFromCart(cartId, token)
        retrofit.enqueue(object : Callback<ApiResponse<Cart>>{
            override fun onResponse(
                call: Call<ApiResponse<Cart>>,
                response: Response<ApiResponse<Cart>>
            ) {
                if(response.body() != null){
                    callback(response.body()!!)
                }else{
                    val json = response.errorBody()!!.toString()
                    var gson = Gson()
                    val result = gson.fromJson<ApiResponse<Cart>>(
                        json,
                        object : TypeToken<ApiResponse<Cart>>() {}.type
                    )
                    callback(result)
                }
            }

            override fun onFailure(call: Call<ApiResponse<Cart>>, t: Throwable) {
                Log.e(LoginActivity.TAG, "Api register call failed message: " + t.message)
            }
        })
    }

    fun updateCartItemQuantity(
        cartId: Int,
        quantity: Int,
        token: String,
        callback: (ApiResponse<Cart>) -> Unit
    ){
        val updateCartQuantityBody = UpdateCartQuantityBody(
            cartId = cartId,
            quantity = quantity
        )
        val retrofit = RetrofitClient.cartClient.updateCartQuantity(updateCartQuantityBody, token)
        retrofit.enqueue(object : Callback<ApiResponse<Cart>>{
            override fun onResponse(
                call: Call<ApiResponse<Cart>>,
                response: Response<ApiResponse<Cart>>
            ) {
                if(response.body() != null){
                    callback(response.body()!!)
                }else{
                    val json = response.errorBody()!!.toString()
                    var gson = Gson()
                    val result = gson.fromJson<ApiResponse<Cart>>(
                        json,
                        object : TypeToken<ApiResponse<Cart>>() {}.type
                    )
                    callback(result)
                }
            }

            override fun onFailure(call: Call<ApiResponse<Cart>>, t: Throwable) {
                Log.e(LoginActivity.TAG, "Api register call failed message: " + t.message)
            }
        })
    }

}