package io.github.sobaau.weather.ui.components

import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import io.github.sobaau.weather.ui.theme.MainAppBar
import io.github.sobaau.weather.ui.theme.WeatherTheme


sealed interface AppNavigation {
    data class WeatherList(
        val destination: String = "weatherList",
        val navController: NavController,
        val navigationIcon: ImageVector
    ) : AppNavigation

    object None : AppNavigation
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherAppBar(
    appNavigation: AppNavigation
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MainAppBar
        ),
        title = {
            Text(
                text = "Weather",
                color = Color.White,
                style = MaterialTheme.typography.h6
            )
        },
        navigationIcon = {
            if (appNavigation is AppNavigation.WeatherList) {
                IconButton(onClick = {
                    appNavigation.navController.navigate(appNavigation.destination) {
                        popUpTo(appNavigation.destination) {
                            inclusive = true
                        }
                    }
                }) {
                    Icon(
                        imageVector = appNavigation.navigationIcon,
                        contentDescription = "Navigate Back",
                        tint = Color.White,
                    )
                }
            }
        })
}

@Preview
@Composable
fun PreviewWeatherAppBar() {
    WeatherTheme {
        WeatherAppBar(AppNavigation.None)
    }
}

@Preview
@Composable
fun PreviewWeatherAppBarNavigation() {
    WeatherTheme {
        WeatherAppBar(
            AppNavigation.WeatherList(
                navController = rememberNavController(),
                navigationIcon = Icons.Default.KeyboardArrowLeft
            )
        )

    }
}