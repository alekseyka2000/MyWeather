package com.example.myweather.model.weather_service

import com.example.myweather.model.entity.ForecastData
import io.reactivex.Single

interface WeatherService {
    fun getWeather(lat: String, lon: String):Single<ForecastData>
}