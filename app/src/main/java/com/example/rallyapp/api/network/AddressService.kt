package com.example.rallyapp.api.network

import com.example.rallyapp.api.dataModel.request_models.AddAddressRequest
import com.example.rallyapp.api.dataModel.response_models.Address
import com.example.rallyapp.api.dataModel.response_models.ApiResponse
import com.example.rallyapp.api.dataModel.response_models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface AddressService {
    companion object{
        const val USER_ID = "user_id"
    }

    @GET("address/user/{${USER_ID}}")
    fun getUsersAddress(
        @Path(USER_ID) userId: Int,
        @Header(CartService.AUTH_HEADER) token: String
    ) : Call<ApiResponse<Address<User>>>

    @POST("address")
    fun addNewAddress(
        @Body addAddressRequest: AddAddressRequest,
        @Header(CartService.AUTH_HEADER) token: String
    ) : Call<ApiResponse<Address<Int>>>

}