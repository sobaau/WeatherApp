package io.github.sobaau.weather.ui.suburb

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController


@Composable
fun SuburbRoute(
    viewModel: SuburbViewModel = hiltViewModel(),
    navController: NavController,
    suburb: String?
) {
    val uiState by viewModel.uiState.collectAsState()
    if (suburb != null) {
        viewModel.getSuburbWeather(suburb)
        SuburbScreen(
            uiState = uiState,
            onRefresh = viewModel::onRefresh,
            navController = navController
        )
    } else {
        navController.navigate("weatherList")
    }
}