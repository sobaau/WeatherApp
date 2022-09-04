package io.github.sobaau.weather

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.sobaau.weather.data.AppDatabase
import io.github.sobaau.weather.data.WeatherDao
import io.github.sobaau.weather.model.WeatherData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class WeatherDaoTest {
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
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        weatherDao = db.weatherDao()
        runBlocking {
            weatherDao.insertAll(weatherList)
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeSuburbAndRead() {
        runBlocking {
            val result = weatherDao.getSuburbWeather("Toowoomba").first()
            assertThat(result.name, equalTo("Toowoomba"))
        }

    }

    @Test
    @Throws(Exception::class)
    fun getWeatherSortedAZ() {
        runBlocking {
            val result = weatherDao.getAllSortedByName().first()
            assertThat(result, equalTo(weatherList.sortedBy { it.name }))
        }
    }

    @Test
    @Throws(Exception::class)
    fun getWeatherAndGetSortedTemperature() {
        runBlocking {
            val result = weatherDao.getAllSortedByTemperature().first()
            assertThat(result, equalTo(weatherList.sortedBy { it.weatherTemp }))
        }
    }

    @Test
    @Throws(Exception::class)
    fun getWeatherAndGetSortedLastUpdated() {
        runBlocking {
            val result = weatherDao.getAllSortedByLastUpdated().first()
            assertThat(result, equalTo(weatherList.sortedBy { it.weatherLastUpdated }))
        }
    }

    @Test
    @Throws(Exception::class)
    fun getWeatherByCountrySortedAZ() {
        runBlocking {
            val result = weatherDao.getByCountrySortByAZ(country = "Australia").first()
            assertThat(result, equalTo(weatherList.filter { it.country == "Australia" }.sortedBy { it.name }))
        }
    }

    @Test
    @Throws(Exception::class)
    fun getWeatherByCountrySortedTemperature() {
        runBlocking {
            val result =
                weatherDao.getByCountrySortByTemperature(country = "Australia").first()
            assertThat(result,
                equalTo(weatherList.filter { it.country == "Australia" }
                    .sortedBy { it.weatherTemp })
            )

        }
    }

    @Test
    @Throws(Exception::class)
    fun getWeatherByCountrySortedLastUpdated() {
        runBlocking {
            val result =
                weatherDao.getByCountrySortByLastUpdated(country = "Australia")
                    .first()
            assertThat(result,
                equalTo(weatherList.filter { it.country == "Australia" }
                    .sortedBy { it.weatherLastUpdated })
            )
        }
    }

}