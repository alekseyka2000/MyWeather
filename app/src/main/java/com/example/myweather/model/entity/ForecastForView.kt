package com.example.myweather.model.entity

data class ForecastForView(
    val date: String,
    val imageURL: String,
    val city: String,
    val country: String,
    val weather: String,
    val temperature: String,
    val humidity: String,
    val precipitation: String,
    val pressure: String,
    val windSpeed: String,
    val directionOfTheWind: String
)
