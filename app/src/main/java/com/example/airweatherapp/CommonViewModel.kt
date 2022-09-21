package com.example.airweatherapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airweatherapp.ResponseStatus.*
import kotlinx.coroutines.launch

class CommonViewModel() : ViewModel() {

    /**
     * Response status
     */
    private val _status = MutableLiveData<ResponseStatus>()
    val status: LiveData<ResponseStatus> = _status

    /**
     * Livedata for the place response
     */
    private val _place: MutableLiveData<Place> = MutableLiveData<Place>()
    val place: LiveData<Place> = _place

    fun getPlace(cityName: String) {
        baseGetPlace(cityName)
    }

    fun getPlace(latitude: Double, longitude: Double) {
        baseGetPlace(latitude, longitude)
    }

    /**
     * General logic for fetching a place
     */
    private fun baseGetPlace(vararg input: Any) {
        viewModelScope.launch {
            // Start loading
            _status.value = LOADING

            try {
                // Call appropriate method depending on args
                input.apply {
                    if (size == 1 && get(0) is String) {
                        val cityName = input[0] as String
                        _place.value = WeatherApiImpl.weatherApiService.getPlace(cityName)
                    }
                    if (size == 2 && all { it is Double }) {
                        val (latitude, longitude) = map { it as Double }
                        _place.value =
                            WeatherApiImpl.weatherApiService.getPlace(latitude, longitude)
                    }
                }
                // Success
                _status.value = DONE
            } catch (e: Exception) {
                // Error
                _status.value = ERROR
                e.printStackTrace()
            }
        }

    }

}