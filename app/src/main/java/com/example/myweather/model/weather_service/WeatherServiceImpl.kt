package com.example.myweather.model.weather_service

import com.example.myweather.model.entity.ForecastData
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class WeatherServiceImpl: WeatherService {
    override fun getWeather(location: Pair<String, String>): Single<ForecastData> =
        Retrofit.Builder()
            .baseUrl("https://samples.openweathermap.org/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherAPI::class.java)
            .getForecast(location.first, location.second)
}