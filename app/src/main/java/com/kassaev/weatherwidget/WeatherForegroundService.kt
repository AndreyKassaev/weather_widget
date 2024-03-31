package com.kassaev.weatherwidget

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import com.google.android.gms.location.LocationServices
import com.kassaev.weatherwidget.data.WeatherApiService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class WeatherForegroundService: Service() {

    @Inject lateinit var viewModel: WeatherViewModel
    @Inject lateinit var networkObserver: NetworkObserver
    var isNetworkEnabled: Boolean = false
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationClient

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        serviceScope.launch {
            networkObserver.observe().collect{
                isNetworkEnabled = it
            }
        }
        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
        start()
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {

        when(intent?.action){
            Actions.START.toString() -> start()
            Actions.STOP.toString() -> stop()
        }

        return super.onStartCommand(
            intent,
            flags,
            startId
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    private fun stop(){
        stopForeground(STOP_FOREGROUND_DETACH)
        stopSelf()
    }

    private fun  start() {
        val interval: Long = 1000 * 60 * 5
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = Notification.Builder(this, "weather_channel")
            .setSmallIcon(R.drawable.logo_svg)
            .setContentTitle("Weather")
            .setOngoing(true)

        locationClient
            .getLocationUpdates(interval)
            .catch { e -> e.printStackTrace() }
            .onEach { location ->
                val formattedLocationString = "${location.latitude},${location.longitude}"

                if (isNetworkEnabled){

                    Locale.getDefault().language
                    val currentWeather  = serviceScope.async {
                        WeatherApiService.weatherApi.getWeatherForecast(formattedLocationString)
                    }.await().toModel()

                    GlanceAppWidgetManager(applicationContext).getGlanceIds(WeatherWidget::class.java).forEach { glanceId ->
                        updateAppWidgetState(applicationContext, glanceId){ prefs ->
                            prefs[stringPreferencesKey("city")] = currentWeather.city
                            prefs[stringPreferencesKey("temp")] = currentWeather.temp
                            prefs[stringPreferencesKey("desc")] = currentWeather.desc
                            prefs[stringPreferencesKey("iconUri")] = currentWeather.iconUri
                        }
                    }
                    val updatedNotification = notification.setContentText(
                        "${currentWeather.city}, ${currentWeather.temp}℃, ${currentWeather.desc}"
                    )
                    notificationManager.notify(
                        1,
                        updatedNotification.build()
                    )
                    WeatherWidget().updateAll(applicationContext)
                    viewModel.currentWeather = currentWeather
                }

            }.launchIn(serviceScope)
        startForeground(1, notification.build())

    }
}
enum class Actions{
    START, STOP
}