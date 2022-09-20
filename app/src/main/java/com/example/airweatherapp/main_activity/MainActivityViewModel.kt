package com.example.airweatherapp.main_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airweatherapp.Place
import com.example.airweatherapp.WeatherApiImpl
import kotlinx.coroutines.launch

class MainActivityViewModel() : ViewModel() {
    
    private val _place: MutableLiveData<Place> = MutableLiveData<Place>()
    val place: LiveData<Place> = _place

    fun getPlace(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                _place.value = WeatherApiImpl.weatherApiService.getPlace(latitude, longitude)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
}