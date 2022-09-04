package io.github.sobaau.weather.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.sobaau.weather.model.WeatherData
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(plants: List<WeatherData>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weatherData: WeatherData)

    @Query("SELECT * FROM weather WHERE country = :country ORDER BY name")
    fun getByCountrySortByAZ(country: String): Flow<List<WeatherData>>

    @Query("SELECT * FROM weather WHERE country = :country ORDER BY weatherTemp")
    fun getByCountrySortByTemperature(country: String): Flow<List<WeatherData>>

    @Query("SELECT * FROM weather WHERE country = :country ORDER BY weatherLastUpdated")
    fun getByCountrySortByLastUpdated(country: String): Flow<List<WeatherData>>

    @Query("SELECT * FROM weather ORDER BY name")
    fun getAllSortedByName(): Flow<List<WeatherData>>

    @Query("SELECT * FROM weather ORDER BY weatherTemp")
    fun getAllSortedByTemperature(): Flow<List<WeatherData>>

    @Query("SELECT * FROM weather ORDER BY weatherLastUpdated")
    fun getAllSortedByLastUpdated(): Flow<List<WeatherData>>

    @Query("SELECT * FROM weather WHERE name = :name ORDER BY name")
    fun getSuburbWeather(name: String): Flow<WeatherData>
}