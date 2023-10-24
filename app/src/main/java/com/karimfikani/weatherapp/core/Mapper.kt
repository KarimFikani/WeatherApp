package com.karimfikani.weatherapp.core

fun interface Mapper<F, T> {
    fun map(from: F): T
}
