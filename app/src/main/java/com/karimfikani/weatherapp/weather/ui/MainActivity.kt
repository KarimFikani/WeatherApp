package com.karimfikani.weatherapp.weather.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.karimfikani.weatherapp.R
import com.karimfikani.weatherapp.core.RxBus
import com.karimfikani.weatherapp.core.RxEvent
import com.karimfikani.weatherapp.core.UiState
import com.karimfikani.weatherapp.databinding.ActivityMainBinding
import com.karimfikani.weatherapp.search.ui.SearchFragment
import com.karimfikani.weatherapp.weather.data.WeatherUiModel
import com.karimfikani.weatherapp.weather.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val weatherViewModel: WeatherViewModel by viewModels()

    private var searchItemDisposable: Disposable? = null

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // load the last weather location that the user searched for
        // If there is no saved location then ideally we would want some visuals and text telling the user
        // that they should search for something in order to see the weather etc.. Will skip this step
        // for this assignment.
        val savedLocation = getSavedLocation()
        if (savedLocation != null) {
            binding.location.text = savedLocation
            weatherViewModel.fetchWeatherByLocation(savedLocation)
        } else {
            checkLocationPermission()
        }

        binding.searchBar.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.fragment,
                        SearchFragment.instance(),
                        SearchFragment.TAG
                    )
                    .addToBackStack(SearchFragment.TAG)
                    .commit()
            }
        }

        setupObservers()
    }

    private fun setupObservers() {
        // Event observer
        searchItemDisposable = RxBus.listen(RxEvent.SearchItemClicked::class.java).subscribe {
            binding.location.text = it.address

            saveLocation(it.address)

            weatherViewModel.fetchWeatherByLocation(it.address)
        }

        // UiState observer
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                weatherViewModel.uiState.collect {
                    when (it) {
                        is UiState.Idle -> {
                            resetView()
                        }

                        is UiState.Loading -> {
                            showLoadingProgress()
                        }

                        is UiState.Error -> {
                            hideLoadingProgress()
                            showErrorMessage()
                        }

                        is UiState.Success -> {
                            resetView()
                            showWeatherResults(it.data)
                        }
                    }
                }
            }
        }
    }

    private fun resetView() {
        hideLoadingProgress()
        hideErrorMessage()
    }

    // common functionalities between fragments can be moved to a base fragment
    private fun showLoadingProgress() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoadingProgress() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showErrorMessage() {
        binding.errorMessage.visibility = View.VISIBLE
    }

    private fun hideErrorMessage() {
        binding.errorMessage.visibility = View.GONE
    }

    private fun showWeatherResults(weatherUiModel: WeatherUiModel) {
        binding.weatherInfo.text = weatherUiModel.info

        Glide.with(this)
            .asBitmap()
            .load(weatherUiModel.iconUrl)
            //.error() -> provide an error image in case glide fails to load url
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(binding.weatherImage)
    }

    override fun onDestroy() {
        super.onDestroy()

        searchItemDisposable?.dispose()
    }

    /**
     * since we are only saving the location and not all the weather data that comes with it then it
     * is fine to use shared preferences since it is simple to use. More complex data would require
     * a DB.
     */
    private fun saveLocation(location: String) {
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(SAVED_LOCATION, location)
            apply()
        }
    }

    private fun getSavedLocation(): String? {
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return null
        return sharedPref.getString(SAVED_LOCATION, null)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                loadWeatherFromDeviceLocation()
            } else {
                // If the user refuses to grant location permission then we don't do anything since they
                // still have the choice of searching for a location manually.
            }
        }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            loadWeatherFromDeviceLocation()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }

    private fun loadWeatherFromDeviceLocation() {
        val lastKnownLocation = getLastKnownLocation() ?: return
        val geocoder = Geocoder(this, Locale.US)
        try {
            val addresses =
                geocoder.getFromLocation(lastKnownLocation.latitude, lastKnownLocation.longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]

                // this is duplicate code similar to the one in SearchModelMapper. Good to move that
                // code into a helper function and write unit tests once and it would be reused
                // multiple times. Since this is a timed assignment will skip it for now.
                val location = StringBuilder()
                location.append("${address.locality}")
                if (address.adminArea != null) {
                    location.append(", ${address.adminArea}")
                }
                if (address.countryCode != null) {
                    location.append(", ${address.countryCode}")
                }

                binding.location.text = location.toString()
                weatherViewModel.fetchWeatherByLocation(location.toString())
            }
        } catch (ignored: IOException) {
            // do nothing
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation(): Location? {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        val providers = locationManager.getProviders(true)
        var bestLocation: Location? = null

        for (provider in providers) {
            val location = locationManager.getLastKnownLocation(provider) ?: continue
            if (bestLocation == null || location.accuracy < bestLocation.accuracy) {
                bestLocation = location
            }
        }
        return bestLocation
    }

    companion object {
        const val SAVED_LOCATION = "saved_location"
    }
}
