package io.github.sobaau.weather.data

import io.github.sobaau.weather.model.WeatherJson
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.gson.*

class WeatherApi {

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            gson()
        }
    }

    /**
     * Get weather data from the given backend
     * @return An ApiResponse with the weather data or an error
     */
    suspend fun getWeatherFromApi(): ApiResponse {
        try {
            val jsonWeatherData: WeatherJson =
                client.get("http://dnu5embx6omws.cloudfront.net/venues/weather.json").body()
            val weatherData = jsonWeatherData.data?.let { jsonWeatherData.buildWeatherData(it) }
            weatherData?.let {
                return ApiResponse.Success(weatherData)
            }
            return ApiResponse.Error("No weather data retrieved from server.")
        } catch (e: Exception) { // Really should handle this better
            return ApiResponse.Error("Connection Error: Please check your internet connection.")
        }
    }
}