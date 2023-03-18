package com.example.rallyapp.api.network

import com.example.rallyapp.api.dataModel.response_models.Menu
import com.example.rallyapp.api.dataModel.response_models.ApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MenuService {
    companion object{
        const val SEARCH_STRING = "search_string"
        const val MENU_ID = "menu_id"
    }

    @GET("menu")
    fun getAllMenuItem(): Call<ApiResponse<Menu>>

    @GET("menu/search/{$SEARCH_STRING}")
    fun searchMenuItems(@Path(SEARCH_STRING) searchString: String): Call<ApiResponse<Menu>>

    @GET("menu/{$MENU_ID}")
    fun getMenuItemById(@Path(MENU_ID) menuId: Int): Call<ApiResponse<Menu>>

}