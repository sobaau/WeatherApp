package io.github.sobaau.weather.ui.suburb

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.sobaau.weather.data.Result
import io.github.sobaau.weather.data.WeatherRepository
import io.github.sobaau.weather.model.WeatherData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

data class SuburbUiState(
    val weatherData: WeatherData? = null,
    val lastUiState: String? = null,
    val isLoading: Boolean = false,
    val snackBarMessage: String = "",
)

@HiltViewModel
class SuburbViewModel @Inject constructor(private val weatherRepository: WeatherRepository) :
    ViewModel() {

    val uiState = MutableStateFlow(SuburbUiState())

    fun snackBarShown() {
        uiState.update { it.copy(snackBarMessage = "") }
    }

    private fun updateWeather() {
        viewModelScope.launch {
            uiState.update { it.copy(isLoading = true) }
            when (val result = weatherRepository.getWeatherFromApi()) {
                is Result.Success -> {
                    uiState.update { it.copy(isLoading = false) }
                }
                is Result.Error -> {
                    uiState.update { it.copy(isLoading = false, snackBarMessage = result.message) }
                }
            }
        }
    }

    fun onRefresh() {
        updateWeather()

    }

    fun getSuburbWeather(suburb: String) {
        viewModelScope.launch {
            weatherRepository.getSuburbWeather(suburb).collect { data ->
                uiState.update {
                    it.copy(
                        weatherData = data,
                        lastUiState = SimpleDateFormat("h:mma d MMMM yyyy").format(Date(data.weatherLastUpdated.toLong() * 1000))
                    )
                }
            }
        }
    }
}
