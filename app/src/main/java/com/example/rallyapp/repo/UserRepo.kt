package com.example.rallyapp.repo

import android.content.Context
import android.util.Log
import com.example.rallyapp.LoginActivity
import com.example.rallyapp.SingUpActivity
import com.example.rallyapp.dataModel.*
import com.example.rallyapp.network.RetrofitClient
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
class UserRepo (appContext: Context) {

    suspend fun registerUser(request: RegisterRequest): List<RegisterResponse> {

        return withContext(Dispatchers.IO) {
            // call api with query here from RetrofitClient
            val retrofit = RetrofitClient.registerRetrofit.registerUser(request)
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
            val retrofit = RetrofitClient.loginRetrofit.loginUser(request)
            val result = mutableListOf<LoginResponse>()
            retrofit.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse?>,
                    response: Response<LoginResponse?>
                ) {
                    var responseBody = response.body()!!
                    result.add(responseBody)
                    LoginActivity.viewModel.setLoginData(result)

                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.e(LoginActivity.TAG, "API login call failed message: " + t.message)
                }
            })
            result
        }
    }

}
