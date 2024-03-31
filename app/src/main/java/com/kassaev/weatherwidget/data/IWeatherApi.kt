package com.kassaev.weatherwidget.data

import retrofit2.http.GET
import retrofit2.http.Query

interface IWeatherApi {
    @GET("current.json?key=5a5667212db44b7c8f195312242603&aqi=no&lang=ru")
    suspend fun getWeatherForecast(@Query("q") location: String): WeatherEntity
}