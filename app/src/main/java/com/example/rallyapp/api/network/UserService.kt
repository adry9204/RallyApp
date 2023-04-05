package com.example.rallyapp.api.network

import com.example.rallyapp.api.dataModel.*
import com.example.rallyapp.api.dataModel.request_models.UpdateUserRequest
import com.example.rallyapp.api.dataModel.response_models.ApiResponse
import com.example.rallyapp.api.dataModel.response_models.User
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path

interface UserService {

        companion object{
                const val  USERID = "userId"
        }

        @POST("users/register")
        fun registerUser(@Body user: RegisterRequest): Call<RegisterResponse>

        @POST("users/login")
        fun loginUser(@Body user: LoginRequest): Call<LoginResponse>

        @DELETE("users/logout")
        fun logoutUser(@Header("Authorization") token: String): Call<LogoutResponse>

        @PATCH("users")
        fun updateUser(@Body user: UpdateUserRequest, @Header("Authorization") token: String): Call<ApiResponse<User>>

        @GET("users/{${USERID}}")
        fun getUserById(@Path(USERID) userId: Int,  @Header("Authorization") token: String): Call<ApiResponse<User>>

}