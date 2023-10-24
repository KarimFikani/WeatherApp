package com.karimfikani.weatherapp.search.data

import com.squareup.moshi.Json

data class LocalNames(
    val ar: String?,
    val cs: String?,
    val de: String?,
    val el: String?,
    val en: String?,
    val eo: String?,
    val es: String?,
    val fr: String?,
    val he: String?,
    val hu: String?,
    @Json(name = "it")
    val itl: String?,
    val ka: String?,
    val ko: String?,
    val la: String?,
    val lt: String?,
    val lv: String?,
    val mk: String?,
    val pt: String?,
    val ro: String?,
    val ru: String?,
    val sk: String?,
    val sr: String?,
    val uk: String?,
    val zh: String?
)
