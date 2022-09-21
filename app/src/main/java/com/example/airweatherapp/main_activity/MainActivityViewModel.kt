package com.example.airweatherapp.main_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airweatherapp.Place
import com.example.airweatherapp.ResponseStatus
import com.example.airweatherapp.ResponseStatus.*
import com.example.airweatherapp.WeatherApiImpl
import kotlinx.coroutines.launch

class MainActivityViewModel() : ViewModel() {

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

    fun getPlace(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _status.value = LOADING
            try {
                _place.value = WeatherApiImpl.weatherApiService.getPlace(latitude, longitude)
                _status.value = DONE
            } catch (e: Exception) {
                _status.value = ERROR
                e.printStackTrace()
            }
        }
    }
    
}