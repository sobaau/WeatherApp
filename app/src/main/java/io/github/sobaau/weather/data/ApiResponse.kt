package io.github.sobaau.weather.data

import io.github.sobaau.weather.model.WeatherData

sealed class ApiResponse {
    data class Success(val data: List<WeatherData>) : ApiResponse()
    data class Error(val error: String) : ApiResponse()
}
