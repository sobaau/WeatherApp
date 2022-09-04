package io.github.sobaau.weather.ui.weatherlist

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SnackbarResult
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WeatherListRoute(
    viewModel: WeatherListViewModel = hiltViewModel(),
    onNavigateToSuburb: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val weatherData by viewModel.weatherData.collectAsState(emptyList())
    val scaffoldState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    if (!uiState.snackBarMessage.isNullOrBlank() && scaffoldState.snackbarHostState.currentSnackbarData == null) {
        coroutineScope.launch {
            val snackbarResult = uiState.snackBarMessage?.let {
                scaffoldState.snackbarHostState.showSnackbar(
                    it,
                    actionLabel = "Ok"
                )
            }
            when (snackbarResult) {
                SnackbarResult.Dismissed -> viewModel.onSnackBarShown()
                else -> viewModel.onSnackBarShown()
            }

        }
    }
    WeatherListScreen(
        uiState = uiState,
        weatherData = weatherData,
        scaffoldState = scaffoldState,
        onTabSelected = viewModel::updateSelectedTab,
        onFilterItemClick = viewModel::filterByLocation,
        onRefresh = viewModel::onRefresh,
        onNavigateToSuburb = onNavigateToSuburb,
        coroutineScope = coroutineScope
    )
}