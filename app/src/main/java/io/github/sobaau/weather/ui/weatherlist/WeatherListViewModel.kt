package io.github.sobaau.weather.ui.weatherlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.sobaau.weather.data.Result
import io.github.sobaau.weather.data.SortOrder
import io.github.sobaau.weather.data.WeatherRepository
import io.github.sobaau.weather.model.Countries
import io.github.sobaau.weather.model.WeatherData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

data class WeatherListUiState(
    val selectedTab: Int = 0,
    val weatherTabs: List<String> = listOf(
        "A-Z",
        "Temperature",
        "Last Updated"
    ),
    val isLoading: Boolean = false,
    val snackBarMessage: String? = null,
    var weatherData: List<WeatherData> = emptyList(),
    val countryList: List<String> = Countries.LIST,
    val locationFilter: String = "",
) {

    fun sortOrder() = when (weatherTabs[selectedTab]) {
        "A-Z" -> SortOrder.ByName(locationFilter)
        "Temperature" -> SortOrder.ByTemperature(locationFilter)
        "Last Updated" -> SortOrder.ByLastUpdated(locationFilter)
        else -> {
            SortOrder.ByName(locationFilter)
        }
    }
}

@HiltViewModel
class WeatherListViewModel @Inject constructor(private val weatherRepository: WeatherRepository) :
    ViewModel() {

    private val _uiState = MutableStateFlow(
        WeatherListUiState()
    )

    private var _weatherData: MutableStateFlow<SortOrder> = MutableStateFlow(SortOrder.ByName())
    val uiState = _uiState as StateFlow<WeatherListUiState>


    @OptIn(ExperimentalCoroutinesApi::class)
    val weatherData = _weatherData.flatMapLatest {
        _uiState.update { ui -> ui.copy(isLoading = true) }
        weatherRepository.getWeather(it).also {
            _uiState.update { ui -> ui.copy(isLoading = false) }
        }

    }

    init {
        updateWeather()
    }

    private fun updateWeather() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = weatherRepository.getWeatherFromApi()) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            snackBarMessage = "List last updated:  ${Calendar.getInstance().time}"
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update { it.copy(isLoading = false, snackBarMessage = result.message) }
                }
            }
        }
    }


    fun onSnackBarShown() {
        _uiState.update { ui -> ui.copy(snackBarMessage = "") }
    }

    fun updateSelectedTab(selectedTab: Int) {
        _uiState.update {
            it.copy(
                selectedTab = selectedTab,
            )
        }
        _weatherData.value = _uiState.value.sortOrder()
    }

    fun filterByLocation(location: String) {
        _uiState.update {
            it.copy(locationFilter = location)
        }
        _weatherData.value = _uiState.value.sortOrder()
    }

    fun onRefresh() {
        updateWeather()
    }
}