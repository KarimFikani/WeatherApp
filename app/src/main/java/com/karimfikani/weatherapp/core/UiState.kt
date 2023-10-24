package com.karimfikani.weatherapp.core

/**
 * [UiState] used for state flows defined in view models to send back the state to UI
 */
sealed interface UiState<out R> {
    /**
     * Idle state / Starting state
     */
    object Idle: UiState<Nothing>
    /**
     * Loading state
     */
    object Loading : UiState<Nothing>

    /**
     * Error state containing an error message
     */
    data class Error(val errorMessage: String? = null) : UiState<Nothing>

    /**
     * Templated success type holding any data type
     */
    data class Success<T>(val data: T) : UiState<T>
}

val <T> UiState<T>.data: T?
    get() = (this as? UiState.Success)?.data

val <T> UiState<T>.errorMessage: String?
    get() = (this as? UiState.Error)?.errorMessage
