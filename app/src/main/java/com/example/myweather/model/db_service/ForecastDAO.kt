package com.example.myweather.model.db_service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myweather.model.entity.ForecastForView

@Dao
interface ForecastDAO {
    @Query ("select * from forecast_table")
    fun getAll(): List<ForecastForView>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(forecast: ForecastForView)

    @Query("DELETE FROM forecast_table")
    suspend fun deleteAll()

}