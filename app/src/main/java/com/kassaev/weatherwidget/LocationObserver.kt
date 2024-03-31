package com.kassaev.weatherwidget

import android.content.Context
import android.location.LocationManager
import android.location.LocationProvider
import android.os.Build
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LocationObserver(context: Context) {

    private val locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    fun observe(): Flow<Boolean> =
        flow {
            while (true){
                emit(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                delay(1000 * 5)
            }
        }

}