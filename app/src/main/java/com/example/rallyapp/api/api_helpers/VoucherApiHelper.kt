package com.example.rallyapp.api.api_helpers

import android.util.Log
import com.example.rallyapp.activities.LoginActivity
import com.example.rallyapp.api.dataModel.request_models.AddAddressRequest
import com.example.rallyapp.api.dataModel.response_models.Address
import com.example.rallyapp.api.dataModel.response_models.ApiResponse
import com.example.rallyapp.api.dataModel.response_models.Cart
import com.example.rallyapp.api.dataModel.response_models.Voucher
import com.example.rallyapp.api.network.RetrofitClient
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VoucherApiHelper {
    companion object{
        const val TAG = "VoucherApiHelper"
    }

    fun getUsersVoucher(
        userId: Int,
        authorizationToken: String,
        callback: (ApiResponse<Voucher>) -> Unit
    ){
        val retrofit = RetrofitClient.voucherClient.getUsersVoucher(userId, authorizationToken)
        retrofit.enqueue(object : Callback<ApiResponse<Voucher>> {
            override fun onResponse(
                call: Call<ApiResponse<Voucher>>,
                response: Response<ApiResponse<Voucher>>
            ) {
                if(response.body() != null){
                    val result = response.body()!!
                    callback(result)
                }else{
                    val errorBodyString = response.errorBody()?.string() ?: ""
                    val errorMessage = JSONObject(errorBodyString).getString("message")
                    callback(ApiResponse(
                        success = 0,
                        message = errorMessage,
                        data = listOf()
                    ))
                }
            }
            override fun onFailure(call: Call<ApiResponse<Voucher>>, t: Throwable) {
                Log.e(LoginActivity.TAG, "Api register call failed message: " + t.message)
                callback(ApiResponse(
                    success = 0,
                    message = t.message.toString(),
                    data = listOf()
                ))
            }
        })
    }

}
