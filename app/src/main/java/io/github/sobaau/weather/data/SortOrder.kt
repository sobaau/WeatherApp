package io.github.sobaau.weather.data

sealed class SortOrder(val location: String?) {
    class ByName(location: String? = "") : SortOrder(location)
    class ByLastUpdated(location: String? = "") : SortOrder(location)
    class ByTemperature(location: String? = "") : SortOrder(location)
}
