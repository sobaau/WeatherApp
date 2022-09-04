package io.github.sobaau.weather.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Weather data class represents the weather data from the API.
 */
@Entity(tableName = "weather")
data class WeatherData(
    @PrimaryKey @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "country")
    val country: String,

    @ColumnInfo(name = "weatherCondition")
    val weatherCondition: String,

    @ColumnInfo(name = "weatherWind")
    val weatherWind: String,

    @ColumnInfo(name = "weatherHumidity")
    val weatherHumidity: String,

    @ColumnInfo(name = "weatherTemp")
    val weatherTemp: Int,

    @ColumnInfo(name = "weatherFeelsLike")
    val weatherFeelsLike: Int,

    @ColumnInfo(name = "weatherLastUpdated")
    val weatherLastUpdated: Int
)

