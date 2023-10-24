package com.karimfikani.weatherapp.search.data.mapper

import com.karimfikani.weatherapp.core.Mapper
import com.karimfikani.weatherapp.search.data.LocationItem
import com.karimfikani.weatherapp.search.data.SearchUiModel
import javax.inject.Inject

class SearchModelMapper @Inject constructor() : Mapper<List<LocationItem>, SearchUiModel> {

    override fun map(from: List<LocationItem>): SearchUiModel {
        val locations = from.map {
            val location = StringBuilder()
            location.append("${it.name}")
            if (it.state != null) {
                location.append(", ${it.state}")
            }
            if (it.country != null) {
                location.append(", ${it.country}")
            }

            location.toString()
        }
        return SearchUiModel(locations = locations)
    }
}
