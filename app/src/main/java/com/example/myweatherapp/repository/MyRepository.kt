package com.example.myweatherapp.repository

import com.example.myweatherapp.base.Result
import com.example.myweatherapp.helper.Constants.API_KEY
import com.example.myweatherapp.helper.RetrofitClient

class MyRepository {

    suspend fun getCurrentWeather(cityName: String?) =
        try {
            val result = RetrofitClient.getApi()?.getCurrentWeather(cityName, API_KEY)?.await()
            result?.let {
                Result.Success(it)
            } ?: run {
                { Result.Error("empty response") }
            }
        } catch (e: Throwable) {
            e.message?.let { Result.Error(it) }
        }

    suspend fun getWeatherReport(lat: String?, lon: String?) =
        try {
            val result = RetrofitClient.getApi()?.getWeatherReport(lat, lon, API_KEY)?.await()
            result?.let {
                Result.Success(it)
            } ?: run {
                { Result.Error("empty response") }
            }
        } catch (e: Throwable) {
            e.message?.let { Result.Error(it) }
        }
}