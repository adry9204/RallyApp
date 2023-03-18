package com.example.rallyapp.repo

import android.util.Log
import com.example.rallyapp.LoginActivity
import com.example.rallyapp.api.dataModel.response_models.ApiResponse
import com.example.rallyapp.api.dataModel.response_models.Category
import com.example.rallyapp.api.dataModel.response_models.Menu
import com.example.rallyapp.api.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryRepo {

    fun getAllMenu(callback: (List<Category>) -> Unit){
        val retrofit = RetrofitClient.categoryClient.getAllCategoryItems()
        retrofit.enqueue(object : Callback<ApiResponse<Category>> {
            override fun onResponse(
                call: Call<ApiResponse<Category>>,
                response: Response<ApiResponse<Category>>
            ) {
                if(response.body() != null){
                    val result = response.body()!!.data
                    callback(result)
                }else{
                    val result = listOf<Category>()
                    callback(result)
                }
            }
            override fun onFailure(call: Call<ApiResponse<Category>>, t: Throwable) {
                Log.e(LoginActivity.TAG, "Api register call failed message: " + t.message)
            }
        })
    }
}