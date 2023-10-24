package com.karimfikani.weatherapp.weather.data

import com.karimfikani.weatherapp.core.Mapper
import java.lang.StringBuilder
import javax.inject.Inject

class WeatherModelMapper @Inject constructor() : Mapper<WeatherDto, WeatherUiModel> {

    override fun map(from: WeatherDto): WeatherUiModel {
        val info = StringBuilder()

        val description = from.weather?.firstOrNull()?.description
        description?.let { info.appendLine(it) }

        // Unit shouldn't be hardcoded and should be read from user preferences or system preferences
        // if they exist. For this assignment I'm just hardcoding it.
        val unit = "\u2103"

        from.main?.let { main ->
            main.temp?.let {
                info.appendLine("Temp: ${it.toInt()}$unit")
            }
            main.feelsLike?.let {
                info.appendLine("Feels like: ${it.toInt()}$unit")
            }
            main.tempMin?.let {
                info.appendLine("Min temp: ${it.toInt()}$unit")
            }
            main.tempMax?.let {
                info.appendLine("Max temp: ${it.toInt()}$unit")
            }
        }

        // ideally we should convert degrees value that we are getting from BE into a more user friendly
        // format like N or NW or NE etc...
        from.wind?.speed?.let { info.appendLine("Wind speed: ${it.toInt()}") }

        // I can display a lot more info that is helpful for the user like sunrise/sunset and visibility
        // but for this assignment I will keep it simple.

        val icon = from.weather?.firstOrNull()?.icon ?: ""

        return WeatherUiModel(
            info = info.toString(),
            // this string should not be hardcoded here and instead read from config or the least
            // have it read from resources. Also the resolution is hardcoded here and seems like
            // the backend does not support different resolutions but returning an SVG instead of
            // a PNG would be way better for scalability for Android.
            iconUrl = "https://openweathermap.org/img/wn/$icon@2x.png"
        )
    }
}
