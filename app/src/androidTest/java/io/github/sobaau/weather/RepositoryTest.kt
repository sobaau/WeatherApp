package io.github.sobaau.weather

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.sobaau.weather.data.*
import io.github.sobaau.weather.model.WeatherData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RepositoryTest {
    private lateinit var repo: WeatherRepositoryImp
    private lateinit var weatherDao: WeatherDao
    private lateinit var db: AppDatabase
    private val weatherList = listOf(
        WeatherData(
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
        ),
    )

    @Before
    fun createRepository() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        weatherDao = db.weatherDao()
        runBlocking {
            weatherDao.insertAll(weatherList)
        }

        repo = WeatherRepositoryImp(weatherDao, WeatherApi())
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun testGetWeatherByAZ() {
        runBlocking {
            val weather = repo.getWeather(SortOrder.ByName()).first()
            assertThat(weather, equalTo(weatherList.sortedBy { it.name }))
        }
    }

    @Test
    fun testGetWeatherByTemp() {
        runBlocking {
            val weather = repo.getWeather(SortOrder.ByTemperature()).first()
            assertThat(weather, equalTo(weatherList.sortedBy { it.weatherTemp }))
        }
    }

    @Test
    fun testGetWeatherByCountry() {
        runBlocking {
            val weather = repo.getWeather(SortOrder.ByName("Australia")).first()
            assertThat(weather, equalTo(weatherList.filter { it.country == "Australia" }.sortedBy { it.name }))
        }
    }

    @Test
    fun testGetWeatherByCountryTemp() {
        runBlocking {
            val weather = repo.getWeather(SortOrder.ByTemperature("Australia")).first()
            weather.forEach {
                assertThat(it.country, equalTo("Australia"))
            }
            assertThat(weather, equalTo(weatherList.filter { it.country == "Australia" }.sortedBy { it.weatherTemp }))
        }
    }

    @Test
    fun testGetWeatherByCountryAZ() {
        runBlocking {
            val weather = repo.getWeather(SortOrder.ByName("Australia")).first()
            weather.forEach {
                assertThat(it.country, equalTo("Australia"))
            }
            assertThat(weather, equalTo(weatherList.filter { it.country == "Australia" }.sortedBy { it.name }))
        }
    }

    @Test
    fun getWeatherFromApi() {
        runBlocking {
            val result = repo.getWeatherFromApi()
            assertThat(result, equalTo(Result.Success))
        }
    }
}