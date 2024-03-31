package com.kassaev.weatherwidget.domain

data class WeatherModel(
    val city: String,
    val temp: String,
    val desc: String,
    val iconUri: String
) {
    constructor(): this(
        city = "",
        temp = "",
        desc = "",
        iconUri = ""
    )
}