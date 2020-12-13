package com.example.myweather.di

import com.example.myweather.model.Repository
import com.example.myweather.views.weather_list.ForecastViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val repositoryModule = module {

    single { Repository(context = get()) }
    viewModel { ForecastViewModel(repository = get()) }

}