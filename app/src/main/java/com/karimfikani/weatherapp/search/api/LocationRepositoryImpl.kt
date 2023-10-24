package com.karimfikani.weatherapp.search.api

import com.karimfikani.weatherapp.search.data.LocationItem
import com.karimfikani.weatherapp.core.extension.toResult
import javax.inject.Inject

/**
 * If we had local data to read from and remote data to fetch from then a good practice is to have
 * a local data source and remote data source and the repository would decide from which data source
 * it would need to fetch data and write back to local data source.
 *
 * In our case we we don't store weather data locally since it needs to be live and most recent so
 * we can have the repo act as the remote data store.
 */
class LocationRepositoryImpl @Inject constructor(
    private val locationApi: LocationApi
) : LocationRepository {

    override suspend fun getLocationByName(name: String): Result<List<LocationItem>> {
        // Ideally we should send back a message saying that user should input city and it can't be empty
        if (name.isBlank()) return Result.failure(IllegalStateException("getLocationByName failed"))

        return try {
            locationApi.getLocationByName(cityName = name).toResult()
        } catch (e: Exception) {
            Result.failure(IllegalStateException("getLocationByName failed", e))
        }
    }
}
