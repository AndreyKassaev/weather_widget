package com.kassaev.weatherwidget

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.actionStartService
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import java.io.File

class WeatherWidget: GlanceAppWidget() {
    override var stateDefinition = CustomGlanceStateDefinition

    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        provideContent {

            val prefs = currentState<Preferences>()

            Box(
                modifier = GlanceModifier
                    .appWidgetBackground()
                    .fillMaxSize()
                    .background(Color(0xBEFFFFFF))
                    .clickable(
                        actionStartService<WeatherForegroundService>(
                            isForegroundService = true
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (prefs[stringPreferencesKey("city")] != null){
                    Column(
                        verticalAlignment = Alignment.Vertical.CenterVertically,
                        horizontalAlignment = Alignment.Horizontal.CenterHorizontally
                    ) {
                        Text(
                            text = "${prefs[stringPreferencesKey("city")]}"
                        )
                        Text(
                            text = "${prefs[stringPreferencesKey("temp")]}" + " \u2103",
                            style = TextStyle(
                                fontSize = 32.sp
                            )
                        )
                        Row {
                            Text(
                                text = "${prefs[stringPreferencesKey("desc")]}"
                            )
                        }
                    }
                } else {
                    Text(text = "Tap me!")
                }
            }
        }
    }
    companion object {

        object CustomGlanceStateDefinition : GlanceStateDefinition<Preferences> {
            override suspend fun getDataStore(context: Context, fileKey: String): DataStore<Preferences> {
                return context.dataStore
            }

            override fun getLocation(context: Context, fileKey: String): File {
                // Note: The Datastore Preference file resides is in the context.applicationContext.filesDir + "datastore/"
                return File(context.applicationContext.filesDir, "datastore/$fileName")
            }

            private const val fileName = "widget_store"
            private val Context.dataStore: DataStore<Preferences>
                    by preferencesDataStore(name = fileName)
        }

    }

}


class WeatherWidgetReceiver : GlanceAppWidgetReceiver(){
    override val glanceAppWidget: GlanceAppWidget
        get() = WeatherWidget()
}


