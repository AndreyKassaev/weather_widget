package com.kassaev.weatherwidget

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import com.kassaev.weatherwidget.ui.theme.WeatherWidgetTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var networkObserver: NetworkObserver
    @Inject lateinit var locationObserver: LocationObserver
    @Inject lateinit var viewModel: WeatherViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            0
        )
        Intent(
            applicationContext,
            WeatherForegroundService::class.java
        ).apply {
            action = Actions.START.toString()
            setPackage(packageName)
            startService(this)
        }

        setContent {

            WeatherWidgetTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val networkStatus by networkObserver.observe().collectAsState(initial = false)
                    val locationStatus by locationObserver.observe().collectAsState(initial = false)
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Network is " + if(networkStatus)"ON" else "OFF"
                        )
                        Text(
                            text = "GPS is " + if(locationStatus)"ON" else "OFF"
                        )
                        Text(
                            text = "${viewModel.currentWeather?.city}"
                        )
                        Text(
                            text = "${viewModel.currentWeather?.temp}"
                        )
                        Text(
                            text = "${viewModel.currentWeather?.desc}"
                        )
                    }
                }
            }
        }
    }
}
