package com.karimfikani.weatherapp.search.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocationItem(
    val country: String? = null,
    val lat: Double? = null,
    @Json(name = "local_names")
    val localNames: LocalNames? = null,
    val lon: Double? = null,
    val name: String? = null,
    val state: String? = null
)
