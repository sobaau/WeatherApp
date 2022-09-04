package io.github.sobaau.weather.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.github.sobaau.weather.ui.theme.WeatherTheme

@Composable
fun ErrorContent() {
    Column {

    }
}

@Preview
@Composable
fun PreviewContent() {
    WeatherTheme {
        ErrorContent()
    }
}