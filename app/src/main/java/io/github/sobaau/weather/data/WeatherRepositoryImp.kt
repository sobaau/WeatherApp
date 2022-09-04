package io.github.sobaau.weather.data

import io.github.sobaau.weather.model.WeatherData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepositoryImp @Inject constructor(
    private val weatherDao: WeatherDao,
    private val weatherApi: WeatherApi
) : WeatherRepository {

    /**
     * Get weather data from the backend api and save it to the database.
     * @return A Successful result object if the api call succeeded, else a error with message..
     */
    override suspend fun getWeatherFromApi(): Result = withContext(Dispatchers.IO) {
        when (val response = weatherApi.getWeatherFromApi()) {
            is ApiResponse.Success -> {
                weatherDao.insertAll(response.data)
                return@withContext Result.Success
            }
            is ApiResponse.Error -> {
                return@withContext Result.Error(response.error)
            }
        }
    }

    /**
     * Get weather data relating to a given suburb from the database.
     * @param suburb The suburb to get weather data for.
     */
    override suspend fun getSuburbWeather(suburb: String): Flow<WeatherData> {
        return weatherDao.getSuburbWeather(suburb)
    }

    /**
     * Get all weather data from the database.
     * @param sortOrder The order to sort the data by.
     */
    override suspend fun getWeather(sortOrder: SortOrder): Flow<List<WeatherData>> {
        val weatherData =
        if (sortOrder.location.isNullOrBlank()) {
            when (sortOrder) {
                is SortOrder.ByName -> weatherDao.getAllSortedByName()
                is SortOrder.ByTemperature -> weatherDao.getAllSortedByTemperature()
                is SortOrder.ByLastUpdated -> weatherDao.getAllSortedByLastUpdated()
            }
        } else {
            when (sortOrder) {
                is SortOrder.ByName -> weatherDao.getByCountrySortByAZ(
                    sortOrder.location,
                )
                is SortOrder.ByTemperature -> weatherDao.getByCountrySortByTemperature(
                    sortOrder.location,
                )
                is SortOrder.ByLastUpdated -> weatherDao.getByCountrySortByLastUpdated(
                    sortOrder.location,
                )
            }
        }
        return weatherData
    }
}