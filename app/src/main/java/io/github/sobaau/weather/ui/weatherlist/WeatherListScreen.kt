package io.github.sobaau.weather.ui.weatherlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import io.github.sobaau.weather.model.WeatherData
import io.github.sobaau.weather.ui.components.AppNavigation
import io.github.sobaau.weather.ui.components.SuburbOverview
import io.github.sobaau.weather.ui.components.WeatherAppBar
import io.github.sobaau.weather.ui.theme.MainTextColor
import io.github.sobaau.weather.ui.theme.WeatherTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WeatherListScreen(
    uiState: WeatherListUiState,
    weatherData: List<WeatherData>,
    scaffoldState: BottomSheetScaffoldState,
    onTabSelected: (Int) -> Unit,
    onFilterItemClick: (String) -> Unit,
    onNavigateToSuburb: (String) -> Unit,
    onRefresh: () -> Unit,
    coroutineScope: CoroutineScope
) {
    BottomSheetScaffold(
        topBar = {
            WeatherAppBar(AppNavigation.None)
        },
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetPeekHeight = 0.dp,
        scaffoldState = scaffoldState,
        sheetContent = {
            FilterSheet(
                countries = uiState.countryList,
                onFilterClick = onFilterItemClick,
                scaffoldState = scaffoldState,
                coroutineScope = coroutineScope,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            WeatherRail(
                selected = uiState.selectedTab,
                onItemSelected = { selected: Int -> onTabSelected(selected) },
                titles = uiState.weatherTabs
            )
            SwipeRefresh(
                state = rememberSwipeRefreshState(uiState.isLoading),
                onRefresh = { onRefresh() },
                modifier = Modifier
                    .weight(2f)
                    .fillMaxSize()
            ) {
                if (weatherData.isEmpty()) {
                    EmptyContent(onRefresh)
                } else {
                    WeatherDetails(weatherData, onNavigateToSuburb, uiState)
                }
            }
            Divider()
            FilterButton(coroutineScope, scaffoldState)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun FilterButton(
    coroutineScope: CoroutineScope,
    scaffoldState: BottomSheetScaffoldState
) {
    TextButton(
        onClick = {
            coroutineScope.launch {
                when (scaffoldState.bottomSheetState.isCollapsed) {
                    true -> scaffoldState.bottomSheetState.expand()
                    false -> scaffoldState.bottomSheetState.collapse()
                }
            }
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Filter")
    }
}

@Composable
private fun EmptyContent(onRefresh: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "No weather information available.",
            color = MainTextColor,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "Check filter settings or refresh.",
            color = MainTextColor,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
        Button(onClick = { onRefresh() }) {
            Text(text = "Refresh")
        }
    }
}

@Composable
fun WeatherDetails(
    weatherData: List<WeatherData>,
    onNavigateToSuburb: (String) -> Unit = {},
    uiState: WeatherListUiState
) {
    LazyColumn(
        contentPadding = PaddingValues(
            bottom = 4.dp,
            top = 4.dp
        ),
    ) {
        if (weatherData.isEmpty()) {
            item {

            }
        } else {
            items(weatherData) {
                Box {
                    Row(modifier = Modifier.clickable {
                        onNavigateToSuburb(it.name)
                    }) {
                        SuburbOverview(
                            it,
                            modifier = Modifier.padding(start = 36.dp, end = 36.dp),
                            isLoading = uiState.isLoading
                        )
                    }
                }

            }
        }
    }
}


@Composable
fun WeatherRail(selected: Int, onItemSelected: (Int) -> Unit, titles: List<String>) {
    TabRow(
        selectedTabIndex = selected,
    ) {
        titles.forEachIndexed { index, tab ->
            Tab(
                text = {
                    Text(
                        tab,
                        fontWeight = if (selected == index) FontWeight.Bold else FontWeight.Normal,
                        color = MainTextColor
                    )
                },
                selected = selected == index,
                onClick = { onItemSelected(index) },
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilterSheet(
    countries: List<String>,
    onFilterClick: (String) -> Unit,
    modifier: Modifier,
    scaffoldState: BottomSheetScaffoldState,
    coroutineScope: CoroutineScope
) {
    LazyColumn(
        modifier = modifier,
    ) {
        item {
            Row {
                IconButton(onClick = {
                    coroutineScope.launch {
                        scaffoldState.bottomSheetState.collapse()
                    }
                }) {
                    Icon(Icons.Default.Close, contentDescription = "Close Filters")
                }
            }
        }
        items(countries) {
            FilterItem(country = it, onFilterClick, scaffoldState, coroutineScope)
            Divider()
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilterItem(
    country: String,
    onFilterClick: (String) -> Unit,
    scaffoldState: BottomSheetScaffoldState,
    coroutineScope: CoroutineScope
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                coroutineScope.launch {
                    onFilterClick(country)
                    scaffoldState.bottomSheetState.collapse()
                }
            }
            .padding(16.dp)
    ) {
        Text(country, color = MainTextColor)
        Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = MainTextColor)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
fun FilterScreenPreview() {
    WeatherTheme {
        FilterSheet(
            WeatherListUiState().countryList,
            onFilterClick = {},
            modifier = Modifier.fillMaxWidth(),
            scaffoldState = rememberBottomSheetScaffoldState(),
            coroutineScope = rememberCoroutineScope()
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
fun PreviewWeatherListScreen() {
    val we = listOf(
        WeatherData(
            "Toowoomba",
            "Australia",
            "Partly Cloudy",
            "NNW at 11.9 kph ",
            ",82%",
            15,
            15,
            1398729182
        ),
        WeatherData(
            "Ararat",
            "Australia",
            "Partly Cloudy",
            "NNW at 11.9 kph ",
            ",82%",
            15,
            15,
            1398729182
        ),
    )

    WeatherTheme {
        WeatherListScreen(
            uiState = WeatherListUiState(weatherData = we),
            weatherData = we,
            scaffoldState = rememberBottomSheetScaffoldState(),
            onTabSelected = {},
            onFilterItemClick = {},
            onNavigateToSuburb = {},
            onRefresh = {},
            coroutineScope = rememberCoroutineScope()
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
fun PreviewEmptyWeather() {
    val we = emptyList<WeatherData>()
    WeatherTheme {
        WeatherListScreen(
            uiState = WeatherListUiState(weatherData = we),
            weatherData = we,
            scaffoldState = rememberBottomSheetScaffoldState(),
            onTabSelected = {},
            onFilterItemClick = {},
            onNavigateToSuburb = {},
            onRefresh = {},
            coroutineScope = rememberCoroutineScope()
        )
    }
}