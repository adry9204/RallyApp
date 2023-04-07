package com.example.rallyapp.api.network

import com.example.rallyapp.api.dataModel.request_models.AddCartRequestBody
import com.example.rallyapp.api.dataModel.request_models.MakeOrderFromCartRequestBody
import com.example.rallyapp.api.dataModel.response_models.ApiResponse
import com.example.rallyapp.api.dataModel.response_models.Menu
import com.example.rallyapp.api.dataModel.response_models.Order
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface OrderService {
    companion object{
        const val SEARCH_STRING = "search_string"
        const val MENU_ID = "menu_id"
    }

    @POST("orders")
    fun makeOrderFromUsersCart(@Body makeOrderFromCartRequestBody: MakeOrderFromCartRequestBody): Call<ApiResponse<Order>>


}