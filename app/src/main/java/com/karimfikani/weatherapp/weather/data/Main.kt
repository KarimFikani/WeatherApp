package com.karimfikani.weatherapp.weather.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Main(
    @Json(name = "feels_like")
    val feelsLike: Double?,
    @Json(name = "grnd_level")
    val groundLevel: Int?,
    val humidity: Int?,
    val pressure: Int?,
    @Json(name = "sea_level")
    val seaLevel: Int?,
    val temp: Double?,
    @Json(name = "temp_max")
    val tempMax: Double?,
    @Json(name = "temp_min")
    val tempMin: Double?
)
