package com.example.myweatherapp.viewModel

import androidx.lifecycle.MutableLiveData
import com.example.myweatherapp.base.BaseViewModel
import com.example.myweatherapp.base.Result
import com.example.myweatherapp.model.WeatherModel
import com.example.myweatherapp.model.WeatherReport
import com.example.myweatherapp.repository.MyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : BaseViewModel() {
    val successLiveData by lazy { MutableLiveData<WeatherModel>() }
    val successCityReportLiveData by lazy { MutableLiveData<WeatherModel>() }

    val successReportLiveData by lazy { MutableLiveData<WeatherReport>() }

    val errorLiveData by lazy { MutableLiveData<String>() }
    private val myLauncherRepository by lazy { MyRepository() }

    fun getCurrentWeather(cityName: String?) {
        viewModelScope.launch {
            when (val result =
                withContext(Dispatchers.IO) { myLauncherRepository.getCurrentWeather(cityName) }) {
                is Result.Success<*> -> {
                    successLiveData.value = result.data as WeatherModel
                }
                is Result.Error -> {
                    errorLiveData.value = result.exception
                }
            }
        }
    }

    fun getWeatherReport(lat: String?, lon: String?) {
        viewModelScope.launch {
            when (val result =
                withContext(Dispatchers.IO) { myLauncherRepository.getWeatherReport(lat, lon) }) {
                is Result.Success<*> -> {
                    successReportLiveData.value = result.data as WeatherReport
                }
                is Result.Error -> {
                    errorLiveData.value = result.exception
                }
            }
        }
    }
}