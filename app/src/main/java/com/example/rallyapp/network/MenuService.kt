package com.example.rallyapp.network

import com.example.rallyapp.dataModel.Menu
import com.example.rallyapp.dataModel.ApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

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