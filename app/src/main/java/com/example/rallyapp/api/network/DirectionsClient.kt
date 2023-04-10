package com.example.rallyapp.api.network

import com.example.rallyapp.api.dataModel.maps_api.DirectionsApiResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DirectionsClient {
    companion object{

        const val API_KEY = "AIzaSyAzeJzXvX6PRwHwTivNRIf0Z7uEBKX2lvY"
    }

    @GET("maps/api/directions/json")
    fun getDirections(
        @Query("origin")origin: String,
        @Query("destination") destination: String,
        @Query("key") apiKey: String = API_KEY
    ): Call<DirectionsApiResult>
}