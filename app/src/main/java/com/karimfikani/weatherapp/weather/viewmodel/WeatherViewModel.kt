package com.karimfikani.weatherapp.weather.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karimfikani.weatherapp.core.UiState
import com.karimfikani.weatherapp.core.di.IoDispatcher
import com.karimfikani.weatherapp.weather.api.WeatherRepository
import com.karimfikani.weatherapp.weather.data.WeatherModelMapper
import com.karimfikani.weatherapp.weather.data.WeatherUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    // given more time we should inject interfaces and not implementations
    private val weatherModelMapper: WeatherModelMapper,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<WeatherUiModel>>(UiState.Idle)
    val uiState: StateFlow<UiState<WeatherUiModel>> = _uiState.asStateFlow()

    fun fetchWeatherByLocation(location: String) {
        _uiState.update { UiState.Loading }

        viewModelScope.launch(ioDispatcher) {
            val result = weatherRepository.getWeatherByLocation(location)
            if (result.isFailure) {
                _uiState.update {
                    UiState.Error(errorMessage = "Failed to fetch location by name")
                }
            } else {
                _uiState.update {
                    val weatherDto = result.getOrNull()
                    if (weatherDto == null) {
                        UiState.Error(errorMessage = "Failed to fetch location by name")
                    } else {
                        val uiModel = weatherModelMapper.map(weatherDto)
                        UiState.Success(uiModel)
                    }
                }
            }
        }
    }
}
