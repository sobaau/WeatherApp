package io.github.sobaau.weather.data

import io.github.sobaau.weather.model.WeatherData
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getWeatherFromApi(): Result
    suspend fun getSuburbWeather(suburb: String): Flow<WeatherData>
    suspend fun getWeather(sortOrder: SortOrder): Flow<List<WeatherData>>
}