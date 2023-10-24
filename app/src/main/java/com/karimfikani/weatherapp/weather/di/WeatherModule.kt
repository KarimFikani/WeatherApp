package com.karimfikani.weatherapp.weather.di

import android.content.Context
import com.karimfikani.weatherapp.R
import com.karimfikani.weatherapp.core.di.HttpClient
import com.karimfikani.weatherapp.core.extension.addDebugInterceptor
import com.karimfikani.weatherapp.weather.api.WeatherApi
import com.karimfikani.weatherapp.weather.api.WeatherRepository
import com.karimfikani.weatherapp.weather.api.WeatherRepositoryImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


@Module
@InstallIn(ViewModelComponent::class)
object WeatherModule {

    @Provides
    fun providesWeatherRepository(weatherApi: WeatherApi): WeatherRepository {
        return WeatherRepositoryImpl(weatherApi)
    }

    @Provides
    fun providesWeatherApi(
        @ApplicationContext context: Context,
        @HttpClient httpClient: OkHttpClient,
        loggingInterceptor: HttpLoggingInterceptor
    ): WeatherApi {
        val builder = httpClient.newBuilder()
            .addDebugInterceptor(loggingInterceptor)

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        /**
         * For the purpose of this demo we read the base url from resources.
         * Ideally we would read the urls from config and set the url dynamically.
         * In case fetching from config fails then config would fallback to default values.
         */
        return Retrofit.Builder()
            .baseUrl(context.getString(R.string.base_url))
            .client(builder.build())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(WeatherApi::class.java)
    }
}
