package com.kassaev.weatherwidget.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherApiService {
    val baseUri = "https://api.weatherapi.com/v1/"
    val retrofit = Retrofit.Builder()
        .baseUrl(baseUri)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val weatherApi = retrofit.create(IWeatherApi::class.java)
}