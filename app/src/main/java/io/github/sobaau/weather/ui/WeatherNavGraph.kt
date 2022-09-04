package io.github.sobaau.weather.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.github.sobaau.weather.ui.suburb.SuburbRoute
import io.github.sobaau.weather.ui.weatherlist.WeatherListRoute

@Composable
fun WeatherAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "weatherList"
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable("weatherList") {

            WeatherListRoute(
                onNavigateToSuburb = {
                    navController.navigate(
                        "weatherList/$it"
                    )
                }
            )
        }
        composable(
            "weatherList/{suburb}",
            arguments = listOf(navArgument("suburb") {
                type = NavType.StringType
                nullable = false
            })
        ) { backStackEntry ->
            val suburb = backStackEntry.arguments?.getString("suburb")
            SuburbRoute(
                navController = navController,
                suburb = suburb
            )
        }
    }
}
