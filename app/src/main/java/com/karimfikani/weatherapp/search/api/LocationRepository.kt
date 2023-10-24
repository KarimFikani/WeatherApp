package com.karimfikani.weatherapp.search.api

import com.karimfikani.weatherapp.search.data.LocationItem

interface LocationRepository {

    suspend fun getLocationByName(name: String): Result<List<LocationItem>>
}
