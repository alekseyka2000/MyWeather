package com.example.myweather.model

import com.example.myweather.model.entity.ForecastData
import com.example.myweather.model.entity.ForecastForView
import com.example.myweather.model.location_service.LocationService
import com.example.myweather.model.location_service.LocationServiceImpl
import com.example.myweather.model.weather_service.WeatherService
import com.example.myweather.model.weather_service.WeatherServiceImpl
import io.reactivex.schedulers.Schedulers

class Repository {

    private val locationService: LocationService by lazy { LocationServiceImpl() }
    private val weatherService: WeatherService by lazy { WeatherServiceImpl() }

    fun getWeatherData() {
        weatherService.getWeather(locationService.getLocation())
            .map {Mapper().invoke(it) }
            .observeOn(Schedulers.io())
    }

    class Mapper : (ForecastData) -> List<ForecastForView> {
        override fun invoke(data: ForecastData): List<ForecastForView> {
            val newWeatherList = mutableListOf<ForecastForView>()
            data.list.forEach {
                newWeatherList.add(
                    ForecastForView(
                        it.dt_txt,
                        it.weather[0].icon,
                        it.weather[0].main,
                        (it.main.temp.toInt() - 273).toString()
                    )
                )
            }
            return newWeatherList
        }
    }
}