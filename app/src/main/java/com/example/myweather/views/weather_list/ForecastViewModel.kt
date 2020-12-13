package com.example.myweather.views.weather_list

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myweather.model.Repository
import com.example.myweather.model.entity.ForecastForView
import io.reactivex.android.schedulers.AndroidSchedulers

class ForecastViewModel : ViewModel() {

    private val mutableLiveData = MutableLiveData<List<ForecastForView>>()
    val liveData: LiveData<List<ForecastForView>> = mutableLiveData
    private val repository by lazy { Repository() }

    @SuppressLint("CheckResult")
    fun fetchForecastData() {
        repository.getWeatherData()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    mutableLiveData.value = it
                    Log.d("logs", it.toString())
                },
                { Log.d("logs", "${it.message}") }
            )
    }
}