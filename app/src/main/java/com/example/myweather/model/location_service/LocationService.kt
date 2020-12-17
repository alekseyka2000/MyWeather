package com.example.myweather.model.location_service

interface LocationService {
    fun getLocation(): Pair<String, String>
}