package io.github.sobaau.weather.model

import com.google.gson.annotations.SerializedName

/**
 * Weather Json class to model incoming weather json data.
 */
data class WeatherJson(
    val data: List<WeatherJsonData>?
) {
    data class WeatherJsonData(
        @SerializedName("_name")
        val name: String?,
        @SerializedName("_country")
        val country: CountryData?,
        @SerializedName("_weatherCondition")
        val weatherCondition: String?,
        @SerializedName("_weatherWind")
        val weatherWind: String?,
        @SerializedName("_weatherHumidity")
        val weatherHumidity: String?,
        @SerializedName("_weatherTemp")
        val weatherTemp: Int?,
        @SerializedName("_weatherFeelsLike")
        val weatherFeelsLike: Int?,
        @SerializedName("_weatherLastUpdated")
        val weatherLastUpdated: Int?
    ) {
        data class CountryData(
            @SerializedName("_name")
            val name: String?,
        )
    }

    /**
     *  Build a list of Weather objects from the JSON data
     */
    fun buildWeatherData(weatherJson: List<WeatherJsonData>): List<WeatherData> {
        // Assumptions for this filtering: Data is only valid if it has all the fields and last update is not 0
        val weatherData = weatherJson.filter {
            filterValid(it)
        }.map {
            WeatherData(
                name = it.name!!, // Can this really be null? null check above in filter.
                country = it.country?.name!!,
                weatherCondition = it.weatherCondition!!,
                weatherWind = it.weatherWind!!.removePrefix("Wind: "),
                weatherHumidity = it.weatherHumidity!!.removePrefix("Humidity: "),
                weatherTemp = it.weatherTemp!!,
                weatherFeelsLike = it.weatherFeelsLike!!,
                weatherLastUpdated = it.weatherLastUpdated!!
            )
        }
        return weatherData
    }

    /**
     * Filter out invalid data. It is assumed only fields with values are valid.
     * @param weatherJsonData The data to filter.
     */
    private fun filterValid(
        weatherJsonData: WeatherJsonData,
    ): Boolean {
        val isValid: Boolean
        weatherJsonData.apply {
            isValid = (name != null && name.contains("^[a-zA-Z]+$".toRegex())) &&
                    country != null && !country.name.isNullOrBlank() &&
                    weatherCondition != null &&
                    weatherWind != null &&
                    weatherHumidity != null &&
                    weatherTemp != null &&
                    weatherFeelsLike != null &&
                    (weatherLastUpdated != null && weatherLastUpdated > 0)
        }
        return isValid
    }
}