package com.example.rallyapp.utils

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

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
}