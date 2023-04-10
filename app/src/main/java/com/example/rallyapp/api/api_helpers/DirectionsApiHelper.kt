package com.example.rallyapp.api.api_helpers

import android.util.Log
import com.example.rallyapp.activities.LoginActivity
import com.example.rallyapp.api.dataModel.maps_api.DirectionsApiResult
import com.example.rallyapp.api.dataModel.response_models.ApiResponse
import com.example.rallyapp.api.dataModel.response_models.Menu
import com.example.rallyapp.api.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DirectionsApiHelper {

    fun getDirections(origin: String, destination: String, callback: (DirectionsApiResult?) -> Unit){
        val retrofit = RetrofitClient.directionsClient.getDirections(origin, destination)
        retrofit.enqueue(object : Callback<DirectionsApiResult> {
            override fun onResponse(
                call: Call<DirectionsApiResult>,
                response: Response<DirectionsApiResult>
            ) {
                if(response.body() != null){
                    val result = response.body()!!
                    callback(result)
                }else{
                    callback(null)
                }
            }

            override fun onFailure(call: Call<DirectionsApiResult>, t: Throwable) {
                Log.e(LoginActivity.TAG, "Api register call failed message: " + t.message)
                callback(null)
            }

        })
    }
}