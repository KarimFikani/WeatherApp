package com.karimfikani.weatherapp.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @HttpClient
    @Singleton
    @Provides
    fun provideOkHttpClient(
    ): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(
            HttpLoggingInterceptor.Level.BODY
        )
    }

    @DefaultDispatcher
    @Provides
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @IoDispatcher
    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @MainDispatcher
    @Provides
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
}

/**
 * Define an annotation for the http client
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class HttpClient

/**
 * Qualifier for [DefaultDispatcher]
 */
@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class DefaultDispatcher

/**
 * Qualifier for [IoDispatcher]
 */
@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IoDispatcher

/**
 * Qualifier for [MainDispatcher]
 */
@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class MainDispatcher

/**
 * This is a very basic way of authentication and can easily be compromised by reverse engineering
 * the apk.
 */
const val API_KEY = "79e82e9575e1f6c10fc56b6e0d22c721"
