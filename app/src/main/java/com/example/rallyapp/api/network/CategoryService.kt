package com.example.rallyapp.api.network

import com.example.rallyapp.api.dataModel.response_models.ApiResponse
import com.example.rallyapp.api.dataModel.response_models.Category
import retrofit2.Call
import retrofit2.http.GET

interface CategoryService {
    @GET("category")
    fun getAllCategoryItems(): Call<ApiResponse<Category>>
}