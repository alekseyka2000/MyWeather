package com.example.myweather.views.weather_list

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myweather.model.Repository
import com.example.myweather.model.entity.ForecastForView
import io.reactivex.android.schedulers.AndroidSchedulers
import org.koin.core.component.KoinApiExtension

class ForecastViewModel(private val repository: Repository) : ViewModel() {

    private val mutableLiveData = MutableLiveData<Pair<String, List<ForecastForView>>>()
    val liveData: LiveData<Pair<String, List<ForecastForView>>> = mutableLiveData

    @KoinApiExtension
    @SuppressLint("CheckResult")
    fun fetchForecastData() {
        try {
            repository.getWeatherData()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {if (it.second.isNotEmpty()){
                    mutableLiveData.value = it
                    Log.d("logs", it.toString())}
                    else Log.d("logs", "DB is empty turn on the Internet")
                },
                { Log.d("logs", "${it.message}") }
            )}
        catch (ex: Exception ){Log.d("logs", "${ex.message}")}
    }
}