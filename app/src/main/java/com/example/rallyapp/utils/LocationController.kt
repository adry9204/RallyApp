package com.example.rallyapp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng

class LocationController(private var context: Context) {

    private var fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    companion object{
        val RALLY_LOCATION = LatLng(43.7173112591, -79.3055932409)
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(task: (LatLng) -> Unit){
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val usersLocation = LatLng(location.latitude, location.longitude)
                task(usersLocation)
            }
        }
    }
}