package io.github.sobaau.weather.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.sobaau.weather.data.WeatherApi
import io.github.sobaau.weather.data.WeatherDao
import io.github.sobaau.weather.data.WeatherRepository
import io.github.sobaau.weather.data.WeatherRepositoryImp
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {
    @Singleton
    @Provides
    fun provideWeatherRepository(dao: WeatherDao, weatherApi: WeatherApi): WeatherRepository =
        WeatherRepositoryImp(dao, weatherApi)

    @Provides
    fun provideWeatherApi(): WeatherApi = WeatherApi()
}