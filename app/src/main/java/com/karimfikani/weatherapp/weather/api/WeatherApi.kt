package com.karimfikani.weatherapp.weather.api

import com.karimfikani.weatherapp.core.di.API_KEY
import com.karimfikani.weatherapp.search.data.LocationItem
import com.karimfikani.weatherapp.weather.data.WeatherDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("/data/2.5/weather")
    suspend fun getWeatherByLocation(
        @Query("q") cityName: String,
        @Query("units") units: String = "metric",
        @Query("appid") appid: String = API_KEY,
    ): Response<WeatherDto>
}
