package com.karimfikani.weatherapp.core.extension

import retrofit2.Response

fun <T> Response<T>.toResult(): Result<T> {
    val code = this.code()
    return if (this.isSuccessful) {
        Result.success(body()!!)
    } else {
        Result.failure(IllegalStateException("[$code]"))
    }
}
