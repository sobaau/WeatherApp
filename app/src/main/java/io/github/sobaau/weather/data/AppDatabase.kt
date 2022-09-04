package io.github.sobaau.weather.data

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.sobaau.weather.model.WeatherData

@Database(
    entities = [WeatherData::class],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}