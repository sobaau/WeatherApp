package io.github.sobaau.weather.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.material.placeholder
import io.github.sobaau.weather.model.WeatherData
import io.github.sobaau.weather.ui.theme.MainTextColor
import io.github.sobaau.weather.ui.theme.TemperatureColor

@Composable
fun SuburbOverview(weather: WeatherData, modifier: Modifier = Modifier, isLoading: Boolean) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier.fillMaxWidth()
        ) {
            Column {
                Text(
                    text = weather.name,
                    color = MainTextColor,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.placeholder(
                        visible = isLoading,
                        highlight = PlaceholderHighlight.fade()
                    )
                )
                Text(
                    text = weather.weatherCondition,
                    color = MainTextColor,
                    fontSize = 23.sp,
                    modifier = Modifier.placeholder(
                        visible = isLoading,
                        highlight = PlaceholderHighlight.fade()
                    )
                )
            }
            Row(
                Modifier.placeholder(
                    visible = isLoading,
                    highlight = PlaceholderHighlight.fade()
                )
            ) {
                Text(
                    text = weather.weatherTemp.toString(),
                    color = TemperatureColor,
                    fontSize = 50.sp
                )
                Text(text = "°", color = TemperatureColor, fontSize = 50.sp)

            }
        }
        Spacer(modifier = Modifier.padding(8.dp))
        Divider()
    }

}