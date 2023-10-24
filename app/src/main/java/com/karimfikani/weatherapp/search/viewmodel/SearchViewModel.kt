package com.karimfikani.weatherapp.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karimfikani.weatherapp.search.api.LocationRepository
import com.karimfikani.weatherapp.search.data.SearchUiModel
import com.karimfikani.weatherapp.core.UiState
import com.karimfikani.weatherapp.core.di.IoDispatcher
import com.karimfikani.weatherapp.search.data.mapper.SearchModelMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    // given more time we should inject interfaces and not implementations
    private val searchModelMapper: SearchModelMapper,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<SearchUiModel>>(UiState.Idle)
    val uiState: StateFlow<UiState<SearchUiModel>> = _uiState.asStateFlow()

    private var fetchLocation: Job? = null

    /**
     * One other way to use this API is send back locations on every typed letter. That would improve
     * the user experience by not having to print the whole address and just by specifying the first
     * few letters. For the sake of time I will skip this implementation that requires debouncing
     * to minimize the number of API calls.
     */
    fun fetchLocationByName(name: String) {
        _uiState.update { UiState.Loading }

        fetchLocation?.cancel()

        fetchLocation = viewModelScope.launch(ioDispatcher) {
            val result = locationRepository.getLocationByName(name)
            if (result.isFailure) {
                _uiState.update {
                    UiState.Error(errorMessage = "Failed to fetch location by name")
                }
            } else {
                _uiState.update {
                    val uiModel = searchModelMapper.map(result.getOrDefault(emptyList()))
                    UiState.Success(uiModel)
                }
            }
        }
    }
}
