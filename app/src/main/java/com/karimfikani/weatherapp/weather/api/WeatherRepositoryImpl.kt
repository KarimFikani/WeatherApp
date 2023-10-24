package com.karimfikani.weatherapp.weather.api

import com.karimfikani.weatherapp.core.extension.toResult
import com.karimfikani.weatherapp.weather.data.WeatherDto
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi
) : WeatherRepository {

    override suspend fun getWeatherByLocation(location: String): Result<WeatherDto> {
        if (location.isBlank()) return Result.failure(IllegalStateException("getWeatherByLocation failed"))
        return try {
            weatherApi.getWeatherByLocation(location).toResult()
        } catch (e: Exception) {
            Result.failure(IllegalStateException("getWeatherByLocation failed", e))
        }
    }
}
