package com.example.myweather.di

import com.example.myweather.model.Repository
import com.example.myweather.model.db_service.ForecastRoomDB
import com.example.myweather.model.db_service.ForecastRoomDB_Impl
import com.example.myweather.model.location_service.LocationService
import com.example.myweather.model.location_service.LocationServiceImpl
import com.example.myweather.views.weather_list.ForecastViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val repositoryModule = module {

    single { Repository(context = get()) }
    single { ForecastRoomDB.getDatabase(get())}
    viewModel { ForecastViewModel(repository = get()) }
    single { LocationServiceImpl(context = get()) }
}