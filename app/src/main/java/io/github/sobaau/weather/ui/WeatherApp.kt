package io.github.sobaau.weather.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import io.github.sobaau.weather.ui.theme.WeatherTheme

@Composable
fun WeatherApp() {
    val navController = rememberNavController()
    WeatherTheme {
        WeatherAppNavHost(
            navController = navController,
        )
    }
}