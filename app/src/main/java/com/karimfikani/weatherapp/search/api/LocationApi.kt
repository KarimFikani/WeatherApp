package com.karimfikani.weatherapp.search.api

import com.karimfikani.weatherapp.search.data.LocationItem
import com.karimfikani.weatherapp.core.di.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationApi {

    @GET("/geo/1.0/direct")
    suspend fun getLocationByName(
        @Query("q") cityName: String,
        @Query("limit") limit: Int = 5,
        @Query("appid") appid: String = API_KEY,
    ): Response<List<LocationItem>>

    /**
     * given more time I would handle other APIs supporting fetching location like getting location
     * from zipcode/postal code and reverse geocoding.
     */
}
