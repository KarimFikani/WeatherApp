package com.karimfikani.weatherapp.core.extension

import com.karimfikani.weatherapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient

fun OkHttpClient.Builder.addDebugInterceptor(interceptor: Interceptor): OkHttpClient.Builder {
    if (BuildConfig.DEBUG) {
        return addInterceptor(interceptor)
    }
    return this
}
