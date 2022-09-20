package com.example.airweatherapp.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airweatherapp.Place
import com.example.airweatherapp.WeatherApiImpl
import kotlinx.coroutines.launch

class ResultsFragmentViewModel : ViewModel() {
    private val _place: MutableLiveData<Place> = MutableLiveData<Place>()
    val place: LiveData<Place> = _place

    fun getPlace(cityName: String) {
        viewModelScope.launch {
            try {
                _place.value = WeatherApiImpl.weatherApiService.getPlace(cityName)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}