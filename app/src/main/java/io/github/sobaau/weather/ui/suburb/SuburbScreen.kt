package io.github.sobaau.weather.ui.suburb

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import io.github.sobaau.weather.model.WeatherData
import io.github.sobaau.weather.ui.components.AppNavigation
import io.github.sobaau.weather.ui.components.SuburbOverview
import io.github.sobaau.weather.ui.components.WeatherAppBar
import io.github.sobaau.weather.ui.theme.MainTextColor
import io.github.sobaau.weather.ui.theme.TemperatureColor
import io.github.sobaau.weather.ui.theme.WeatherTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuburbScreen(
    uiState: SuburbUiState, onRefresh: () -> Unit, navController: NavController
) {
    Scaffold(topBar = {
        WeatherAppBar(
            AppNavigation.WeatherList(
                navController = navController,
                navigationIcon = Icons.Default.KeyboardArrowLeft
            )
        )
    }) {
        SwipeRefresh(
            modifier = Modifier.padding(it),
            state = rememberSwipeRefreshState(uiState.isLoading),
            onRefresh = { onRefresh() }
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    if (uiState.weatherData != null) {
                        SuburbOverview(uiState.weatherData, isLoading = uiState.isLoading)
                        Spacer(Modifier.padding(top = 16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            FurtherInfo(
                                "Feels like",
                                uiState.weatherData.weatherFeelsLike.toString() + "°",
                                isLoading = uiState.isLoading
                            )
                            FurtherInfo(
                                "Humidity",
                                uiState.weatherData.weatherHumidity,
                                isLoading = uiState.isLoading
                            )
                            FurtherInfo(
                                "Wind",
                                uiState.weatherData.weatherWind,
                                isLoading = uiState.isLoading
                            )
                        }
                        Spacer(Modifier.padding(top = 16.dp))
                        Divider()
                        Spacer(Modifier.padding(top = 16.dp))
                        Text(
                            text = "Lasted updated: ${uiState.lastUiState}",
                            color = MainTextColor
                        )
                    }
                }

            }
        }
    }
}

@Composable
fun FurtherInfo(
    heading: String,
    subText: String,
    modifier: Modifier = Modifier,
    isLoading: Boolean
) {
    Column(modifier = modifier) {
        Text(heading, fontWeight = FontWeight.Bold, color = MainTextColor)
        Text(
            modifier = Modifier.placeholder(
                visible = isLoading,
                highlight = PlaceholderHighlight.fade()
            ), text = subText, color = TemperatureColor
        )
    }
}

@Preview
@Composable
fun PreviewSuburbScreen() {
    WeatherTheme {
        SuburbScreen(
            uiState = SuburbUiState(
                WeatherData(
                    "Wimmera@Ararat",
                    "Australia",
                    "Partly Cloudy",
                    "NNW at 11.9 kph ",
                    ",82%",
                    15,
                    15,
                    1398729182
                )
            ),
            onRefresh = {},
            navController = rememberNavController()
        )
    }
}