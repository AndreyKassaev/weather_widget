package com.kassaev.weatherwidget

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.kassaev.weatherwidget.data.WeatherRepository
import com.kassaev.weatherwidget.domain.WeatherModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
): ViewModel() {

    var currentWeather by mutableStateOf<WeatherModel?>(WeatherModel())

}