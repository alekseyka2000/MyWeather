package com.example.myweather.model.db_service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myweather.model.entity.ForecastForView

@Dao
interface ForecastDAO {
    @Query ("select * from forecast")
    fun getAll(): List<ForecastForView>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(forecast: List<ForecastForView>)

    @Query("DELETE FROM forecast")
    fun deleteAll()

}