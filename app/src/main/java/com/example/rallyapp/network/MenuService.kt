package com.example.rallyapp.network

import com.example.rallyapp.dataModel.Menu
import com.example.rallyapp.dataModel.ApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MenuService {

    @GET("menu")
    fun getAllMenuItem(): Call<ApiResponse<Menu>>

    @GET
    fun searchMenuItems(@Query("search") searchString: String): Call<ApiResponse<Menu>>

}