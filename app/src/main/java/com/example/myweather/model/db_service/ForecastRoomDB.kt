package com.example.myweather.model.db_service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myweather.model.entity.ForecastForView

@Database(entities = [ForecastForView::class], version = 1, exportSchema = false)
abstract class ForecastRoomDB : RoomDatabase() {

    abstract fun forecastDao(): ForecastDAO

    companion object {

        @Volatile
        private var INSTANCE: ForecastRoomDB? = null

        fun getDatabase(context: Context): ForecastRoomDB {
            val tempInstance =
                INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ForecastRoomDB::class.java,
                    "forecast_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
