package com.example.rallyapp.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import com.example.rallyapp.activities.CheckoutActivity


fun CheckoutActivity.registerRequestPermission(task: () -> Unit): ActivityResultLauncher<Array<String>> {
    val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                task()
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                task()
            }
            else -> {

            }
        }
    }
    return locationPermissionRequest
}

class PermissionController(private val context: Context){
    fun handleLocationPermission(
        locationPermissionRequest: ActivityResultLauncher<Array<String>>,
        task: ()-> Unit
    ){
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                task()
            }
            else -> { this.requestPermission(locationPermissionRequest) }
        }
    }

    private fun requestPermission(locationPermissionRequest: ActivityResultLauncher<Array<String>>){
        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ))
    }
}

