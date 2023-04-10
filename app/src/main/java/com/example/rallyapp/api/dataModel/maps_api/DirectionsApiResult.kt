package com.example.rallyapp.api.dataModel.maps_api

data class DirectionsApiResult(
    val geocoded_waypoints: List<GeocodedWaypoint>,
    val routes: List<Route>,
    val status: String
)