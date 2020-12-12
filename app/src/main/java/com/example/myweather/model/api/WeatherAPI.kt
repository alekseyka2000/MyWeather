package com.example.myweather.model.api

import com.example.myweather.model.entity.ForecastData
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    @GET("data/2.5/forecast?appid=439d4b804bc8187953eb36d2a8c26a02")
    fun getForecast(
        @Query("lat") lat: String,
        @Query("lon") lon: String
    ): Single<ForecastData>
}