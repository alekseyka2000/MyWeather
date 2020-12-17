package com.example.myweather.model.location_service

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.myweather.MainActivity
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import org.koin.core.component.KoinApiExtension

class LocationServiceImpl(private val context: Context) : LocationService {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private val locationRequest = LocationRequest().apply {
        interval = 50000
        fastestInterval = 50000
        smallestDisplacement = 100f
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
        }
    }

    @KoinApiExtension
    override fun getLocation() {
        checkPermission()
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    override fun getLM(): Task<Location> {
        checkPermission()
        return fusedLocationClient.lastLocation
    }

    override fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), MainActivity.PERMISSION_REQUEST_CODE
            )
            Log.d("logs", "permission")
        }
    }
}