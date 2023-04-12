package com.example.rallyapp.utils

import android.graphics.Color
import android.util.Log
import androidx.fragment.app.FragmentManager
import com.example.rallyapp.R
import com.example.rallyapp.activities.CheckoutActivity
import com.example.rallyapp.api.dataModel.maps_api.Bounds
import com.example.rallyapp.api.dataModel.maps_api.DirectionsApiResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.*

class CheckoutActivityMapsManager(private val mMap: GoogleMap) {

    companion object {
        const val MARKER_TYPE_RESTAURANT = R.drawable.hotel_icon
        const val MARKER_TYPE_MY_LOCATION = R.drawable.user
    }

    fun addMarker(latLng: LatLng, title: String, type: Int){
       mMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .title(title)
                .icon(BitmapDescriptorFactory.fromResource(type))
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

    fun animateCameraToLocation(swBounds: LatLng, neBounds: LatLng){
        val bound = LatLngBounds(swBounds, neBounds)
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bound, 20))
    }

    suspend fun showDirection(direction: DirectionsApiResult){
        Log.i(CheckoutActivity.TAG, direction.toString())
        var path = mutableListOf<List<LatLng>>()
        withContext(Dispatchers.IO){
            for (steps in direction.routes[0].legs[0].steps){
                val point = steps.polyline.points
                path.add(PolyLineUtil.decodePolyLines(point)!!)
            }

            for (points in path){
                withContext(Dispatchers.Main){
                    mMap.addPolyline(PolylineOptions().addAll(points).color(Color.RED))
                }
            }
            val swBound = LatLng(
                direction.routes[0].bounds.southwest.lat,
                direction.routes[0].bounds.southwest.lng
            )
            val neBound = LatLng(
                direction.routes[0].bounds.northeast.lat,
                direction.routes[0].bounds.northeast.lng
            )
            withContext(Dispatchers.Main){
                animateCameraToLocation(swBound, neBound)
            }
        }

    }
}