package com.example.myweather.model

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.os.Build
import com.example.myweather.model.db_service.ForecastRoomDB
import com.example.myweather.model.entity.ForecastData
import com.example.myweather.model.entity.ForecastForView
import com.example.myweather.model.location_service.LocationService
import com.example.myweather.model.location_service.LocationServiceImpl
import com.example.myweather.model.weather_service.WeatherService
import com.example.myweather.model.weather_service.WeatherServiceImpl
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import java.util.UUID


class Repository(private val context: Context) {
    private val locationService: LocationService by lazy { LocationServiceImpl(context) }
    private val weatherService: WeatherService by lazy { WeatherServiceImpl() }
    val dao = ForecastRoomDB.getDatabase(context).forecastDao()

    @KoinApiExtension
    fun getWeatherData(): Single<Pair<String,List<ForecastForView>>> {
        val connectivityManager =
            context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                return fetchDataByEthernet()
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return fetchDataByEthernet()
            }
        }
        return fetchDataByDB()
    }

    @KoinApiExtension
    private fun fetchDataByEthernet() = weatherService.getWeather(locationService.getLocation())
        .map { Pair("Ethernet on", Mapper().invoke(context, it)) }
        .subscribeOn(Schedulers.io())

    private fun fetchDataByDB() = Single.create<Pair<String, List<ForecastForView>>>
    { emitter -> emitter.onSuccess(Pair("Ethernet off", dao.getAll())) }
        .subscribeOn(Schedulers.io())

    @KoinApiExtension
    inner class Mapper : (Context, ForecastData) -> List<ForecastForView>, KoinComponent {
        override fun invoke(context: Context, data: ForecastData): List<ForecastForView> {
            val newWeatherList = mutableListOf<ForecastForView>()
            dao.deleteAll()
            data.list.forEach {
                newWeatherList.add(
                    ForecastForView(
                        UUID.randomUUID().toString(),
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
                        when ((it.wind.deg * 10).toInt()) {
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
            dao.insert(newWeatherList)
            return newWeatherList
        }
    }
}