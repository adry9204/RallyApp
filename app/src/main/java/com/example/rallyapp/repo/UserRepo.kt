package com.example.rallyapp.repo

import android.content.Context
import android.util.Log
import com.example.rallyapp.activities.LoginActivity
import com.example.rallyapp.activities.SingUpActivity
import com.example.rallyapp.activities.UserActivity
import com.example.rallyapp.api.api_helpers.UserApiHelper
import com.example.rallyapp.api.dataModel.*
import com.example.rallyapp.api.dataModel.request_models.UpdateUserRequest
import com.example.rallyapp.api.dataModel.response_models.ApiResponse
import com.example.rallyapp.api.dataModel.response_models.User
import com.example.rallyapp.api.network.RetrofitClient
import com.example.rallyapp.utils.NetworkHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * UserRepo class
 * Repository to save and get user info
 * author: Satender Yadav
 */
class UserRepo (private val appContext: Context) {

    suspend fun registerUser(request: RegisterRequest): List<RegisterResponse> {

        return withContext(Dispatchers.IO) {
            // call api with query here from RetrofitClient
            val retrofit = RetrofitClient.userRetrofit.registerUser(request)
            val result = mutableListOf<RegisterResponse>()
            retrofit.enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse?>,
                    response: Response<RegisterResponse?>
                ) {
                    if(response.body() != null){
                        result.add(response.body()!!)
                        SingUpActivity.viewModel.setRegisterData(result)
                    } else {
                        val errorBodyString = response.errorBody()?.string() ?: ""
                        val errorMessage = JSONObject(errorBodyString).getString("message")
                        val registerResponse = RegisterResponse(
                            data = listOf(Data("NA", "NA", 2, "NA",false)), // set default values
                            message = errorMessage,
                            success = 1)
                        result.add(registerResponse)
                        SingUpActivity.viewModel.setRegisterData(result)
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Log.e(LoginActivity.TAG, "Api register call failed message: " + t.message)
                }
            })
            result
        }
    }

    suspend fun loginUser(request: LoginRequest): List<LoginResponse> {

        return withContext(Dispatchers.IO) {
            // call api with query here from RetrofitClient
            val retrofit = RetrofitClient.userRetrofit.loginUser(request)
            val result = mutableListOf<LoginResponse>()
            retrofit.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse?>,
                    response: Response<LoginResponse?>
                ) {
                    Log.i("TAG", response.toString())
                    var responseBody = response.body()!!
                    result.add(responseBody)
                    Log.i("TAG", responseBody.toString())
                    LoginActivity.viewModel.setLoginData(result)

                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.e(LoginActivity.TAG, "API login call failed message: " + t.message)
                }
            })
            result
        }
    }

    suspend fun logoutUser(token: String): List<LogoutResponse> {

        return withContext(Dispatchers.IO) {
            // call api with query here from RetrofitClient
            val retrofit = RetrofitClient.userRetrofit.logoutUser(token)
            val result = mutableListOf<LogoutResponse>()
            retrofit.enqueue(object : Callback<LogoutResponse> {
                override fun onResponse(
                    call: Call<LogoutResponse?>,
                    response: Response<LogoutResponse?>
                ) {
                    var responseBody = response.body()!!
                    result.add(responseBody)
                    UserActivity.viewModel.setLogoutData(result)

                }

                override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                    Log.e(UserActivity.TAG, "API logout call failed message: " + t.message)
                }
            })
            result
        }
    }

    suspend fun getUserById(
        userId: Int,
        token: String,
        callback: (response: ApiResponse<User>) -> Unit
    ){

        val networkHelper = NetworkHelper(appContext)
        if(networkHelper.isConnectedToNetwork()){
            withContext(Dispatchers.IO){
                val userApiHelper = UserApiHelper()
                userApiHelper.getUserById(userId, token){
                    callback(it)
                }
            }
        }else{
            withContext(Dispatchers.Main){
                callback(ApiResponse(
                    success = 0,
                    message = "No internet connection",
                    data = listOf()
                ))
            }
        }

    }

    suspend fun updateUser(
        userRequest: UpdateUserRequest,
        token: String,
        callback: (response: ApiResponse<User>) -> Unit
    ){

        val networkHelper = NetworkHelper(appContext)
        if(networkHelper.isConnectedToNetwork()){
            withContext(Dispatchers.IO){
                val userApiHelper = UserApiHelper()
                userApiHelper.updateUserById(userRequest, token){
                    callback(it)
                }
            }
        }else{
            withContext(Dispatchers.Main){
                callback(ApiResponse(
                    success = 0,
                    message = "No internet connection",
                    data = listOf()
                ))
            }
        }
    }

}
