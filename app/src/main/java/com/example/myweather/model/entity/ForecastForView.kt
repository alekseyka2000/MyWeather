package com.example.myweather.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "forecast")
data class ForecastForView(
    @PrimaryKey(autoGenerate = false) var id: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "imageURL") val imageURL: String,
    @ColumnInfo(name = "icon") val city: String,
    @ColumnInfo(name = "country") val country: String,
    @ColumnInfo(name = "weather") val weather: String,
    @ColumnInfo(name = "temperature") val temperature: String,
    @ColumnInfo(name = "humidity") val humidity: String,
    @ColumnInfo(name = "precipitation") val precipitation: String,
    @ColumnInfo(name = "pressure") val pressure: String,
    @ColumnInfo(name = "windSpeed") val windSpeed: String,
    @ColumnInfo(name = "directionOfTheWind")val directionOfTheWind: String
)
