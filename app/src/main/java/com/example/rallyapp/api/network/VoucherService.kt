package com.example.rallyapp.api.network

import com.example.rallyapp.api.dataModel.response_models.ApiResponse
import com.example.rallyapp.api.dataModel.response_models.Voucher
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface VoucherService {

    companion object{
        const val USER_ID = "user_id"
    }

    @GET("voucher/{${USER_ID}}")
    fun getUsersVoucher(
        @Path(USER_ID) userId: Int,
        @Header("Authorization") token: String
    ): Call<ApiResponse<Voucher>>
}