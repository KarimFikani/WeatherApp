package com.karimfikani.weatherapp.weather.api

import com.karimfikani.weatherapp.weather.data.WeatherDto

interface WeatherRepository {

    suspend fun getWeatherByLocation(location: String): Result<WeatherDto>
}
