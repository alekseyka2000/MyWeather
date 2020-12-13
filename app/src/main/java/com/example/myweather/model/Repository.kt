package com.example.myweather.model

import com.example.myweather.model.entity.ForecastData
import com.example.myweather.model.entity.ForecastForView
import com.example.myweather.model.location_service.LocationService
import com.example.myweather.model.location_service.LocationServiceImpl
import com.example.myweather.model.weather_service.WeatherService
import com.example.myweather.model.weather_service.WeatherServiceImpl
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class Repository {

    private val locationService: LocationService by lazy { LocationServiceImpl() }
    private val weatherService: WeatherService by lazy { WeatherServiceImpl() }

    fun getWeatherData(): Single<List<ForecastForView>> =
        weatherService.getWeather(locationService.getLocation())
            .map {Mapper().invoke(it) }
            .subscribeOn(Schedulers.io())

    class Mapper : (ForecastData) -> List<ForecastForView> {
        override fun invoke(data: ForecastData): List<ForecastForView> {
            val newWeatherList = mutableListOf<ForecastForView>()
            data.list.forEach {
                newWeatherList.add(
                    ForecastForView(
                        it.dt_txt,
                        it.weather[0].icon,
                        data.city.name,
                        data.city.country,
                        it.weather[0].description,
                        (it.main.temp.toInt() - 273).toString(),
                        it.main.humidity.toString(),
                        precipitation = "1.0 mm",
                        it.main.pressure.toString(),
                        it.wind.speed.toString(),
                        when((it.wind.deg*10).toInt()){
                            in 0 until 225 -> "N"
                            in 225 until 675 -> "NE"
                            in 675 until 1125 -> "E"
                            in 1125 until 1575 -> "SE"
                            in 1575 until 2025 -> "S"
                            in 2025 until 2475 -> "SW"
                            in 2475 until 2925 -> "W"
                            in 2925 until 3375 -> "NW"
                            in 3375 until 3600 -> "N"
                            else -> "Error"
                        }
                    )
                )
            }
            return newWeatherList
        }
    }
}