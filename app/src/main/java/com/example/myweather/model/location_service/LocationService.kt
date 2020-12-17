package com.example.myweather.model.location_service

import android.location.Location
import com.google.android.gms.tasks.Task

interface LocationService {
    fun getLocation()
    fun getLM(): Task<Location>
    fun checkPermission()
}