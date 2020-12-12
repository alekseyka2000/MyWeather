package com.example.myweather.model.location_service

class LocationServiceImpl: LocationService {
    override fun getLocation(): Pair<String, String> {
        return Pair("0", "0")
    }
}