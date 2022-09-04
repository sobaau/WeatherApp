package io.github.sobaau.weather.data

sealed class Result(val message: String) {
    object Success : Result("")
    class Error(message: String) : Result(message)
}