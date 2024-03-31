package com.kassaev.weatherwidget.di

import android.content.Context
import com.kassaev.weatherwidget.LocationObserver
import com.kassaev.weatherwidget.NetworkObserver
import com.kassaev.weatherwidget.WeatherViewModel
import com.kassaev.weatherwidget.data.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    @Singleton
    fun provideViewModel(repository: WeatherRepository): WeatherViewModel = WeatherViewModel(repository)

    @Provides
    @Singleton
    fun provideRepository(): WeatherRepository = WeatherRepository()

    @Provides
    @Singleton
    fun provideNetworkObserver(@ApplicationContext context: Context): NetworkObserver = NetworkObserver(context)

    @Provides
    @Singleton
    fun provideLocationObserver(@ApplicationContext context: Context): LocationObserver = LocationObserver(context)
}