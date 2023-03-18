package com.example.rallyapp.api.api_helpers

import android.util.Log
import com.example.rallyapp.LoginActivity
import com.example.rallyapp.api.dataModel.response_models.ApiResponse
import com.example.rallyapp.api.dataModel.response_models.Menu
import com.example.rallyapp.api.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuApiHelper {

    fun getAllMenu(callback: (List<Menu>) -> Unit){
        val retrofit = RetrofitClient.menuClient.getAllMenuItem()
        retrofit.enqueue(object : Callback<ApiResponse<Menu>> {
            override fun onResponse(
                call: Call<ApiResponse<Menu>>,
                response: Response<ApiResponse<Menu>>
            ) {
                if(response.body() != null){
                    val result = response.body()!!.data
                    callback(result)
                }else{
                    val result = listOf<Menu>()
                    callback(result)
                }
            }

            override fun onFailure(call: Call<ApiResponse<Menu>>, t: Throwable) {
                Log.e(LoginActivity.TAG, "Api register call failed message: " + t.message)
            }

        })
    }

    fun searchMenu(searchString: String, callback: (List<Menu>) -> Unit){
        val retrofit = RetrofitClient.menuClient.searchMenuItems(searchString)
        retrofit.enqueue(object : Callback<ApiResponse<Menu>> {
            override fun onResponse(
                call: Call<ApiResponse<Menu>>,
                response: Response<ApiResponse<Menu>>
            ) {
                if(response.body() != null){
                    val result = response.body()!!.data
                    callback(result)
                }else{
                    val result = listOf<Menu>()
                    callback(result)
                }
            }

            override fun onFailure(call: Call<ApiResponse<Menu>>, t: Throwable) {
                Log.e(LoginActivity.TAG, "Api register call failed message: " + t.message)
            }

        })
    }

    fun getMenuItemById(menuId: Int, callback: (List<Menu>) -> Unit){
        val retrofit = RetrofitClient.menuClient.getMenuItemById(menuId)
        retrofit.enqueue(object : Callback<ApiResponse<Menu>> {
            override fun onResponse(
                call: Call<ApiResponse<Menu>>,
                response: Response<ApiResponse<Menu>>
            ) {
                if(response.body() != null){
                    val result = response.body()!!.data
                    callback(result)
                }else{
                    val result = listOf<Menu>()
                    callback(result)
                }
            }

            override fun onFailure(call: Call<ApiResponse<Menu>>, t: Throwable) {
                Log.e(LoginActivity.TAG, "Api register call failed message: " + t.message)
            }

        })
    }
}