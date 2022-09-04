package io.github.sobaau.weather

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.github.sobaau.weather.data.SortOrder
import io.github.sobaau.weather.data.WeatherRepository
import io.github.sobaau.weather.model.WeatherData
import io.github.sobaau.weather.ui.weatherlist.WeatherListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock


@RunWith(MockitoJUnitRunner::class)
class WeatherViewModelTest {
    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()
@OptIn(ExperimentalCoroutinesApi::class)
val dispatcher = StandardTestDispatcher()
    private val weatherData = listOf(WeatherData(
        "Toowoomba",
        "Australia",
        "Partly Cloudy",
        "NNW at 11.9 kph ",
        ",82%",
        15,
        15,
        1398729182
    ),
        WeatherData(
            "Brisbane",
            "Australia",
            "Partly Cloudy",
            "NNW at 11.9 kph ",
            ",82%",
            15,
            15,
            1398729182
        ),
        WeatherData(
            "Los Angeles",
            "United States",
            "Partly Cloudy",
            "NNW at 11.9 kph ",
            ",82%",
            12,
            15,
            1398729182
        ),)


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when updateSelectedTab is called, SortOrder should be ByName`() {
        Dispatchers.setMain(dispatcher)
        runBlocking {
        val weatherRepo: WeatherRepository = mock {
        }
        val viewModel = WeatherListViewModel(weatherRepo)
        viewModel.updateSelectedTab(0)
            assertThat(viewModel.uiState.value.sortOrder(), instanceOf(SortOrder.ByName::class.java))

        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when updateSelectedTab is called, SortOrder should be ByTemperature`() {
        Dispatchers.setMain(dispatcher)
        runBlocking {
        val weatherRepo: WeatherRepository = mock {

        }
        val viewModel = WeatherListViewModel(weatherRepo)
        viewModel.updateSelectedTab(1)
            assertThat(viewModel.uiState.value.sortOrder(), instanceOf(SortOrder.ByTemperature::class.java))

        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when updateSelectedTab is called, SortOrder should be ByLastUpdated`() {
        Dispatchers.setMain(dispatcher)
        runBlocking {
        val weatherRepo: WeatherRepository = mock {

        }
        val viewModel = WeatherListViewModel(weatherRepo)
        viewModel.updateSelectedTab(2)
            assertThat(viewModel.uiState.value.sortOrder(), instanceOf(SortOrder.ByLastUpdated::class.java))

        }
    }
}