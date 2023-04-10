package com.example.rallyapp.utils

import android.graphics.Color
import android.util.Log
import androidx.fragment.app.FragmentManager
import com.example.rallyapp.R
import com.example.rallyapp.activities.CheckoutActivity
import com.example.rallyapp.api.dataModel.maps_api.DirectionsApiResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

class CheckoutActivityMapsManager(private val mMap: GoogleMap) {

    fun addMarker(latLng: LatLng){
       mMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .title("Rally Restaurant & Map")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
        )
    }

    fun animateCameraToLocation(latLng: LatLng, zoom: Float){
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                latLng,
                zoom
            )
        )
    }

    fun showDirection(direction: DirectionsApiResult){
        Log.i(CheckoutActivity.TAG, direction.toString())
        var path = mutableListOf<List<LatLng>>()
        for (steps in direction.routes[0].legs[0].steps){
            val point = steps.polyline.points
            path.add(PolyLineUtil.decodePolyLines(point)!!)
        }

        for (points in path){
            mMap.addPolyline(PolylineOptions().addAll(points).color(Color.RED))
        }
    }
}