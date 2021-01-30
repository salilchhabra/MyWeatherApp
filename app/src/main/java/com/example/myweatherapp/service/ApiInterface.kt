package com.example.myweatherapp.service

import com.example.myweatherapp.model.WeatherModel
import com.example.myweatherapp.model.WeatherReport
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("weather")
    fun getCurrentWeather(
        @Query("q") cityName: String?,
        @Query("appid") apikey: String
    ): Deferred<WeatherModel>

    @GET("onecall")
    fun getWeatherReport(
        @Query("lat") lat: String?,
        @Query("lon") lon: String?,
        @Query("appid") apikey: String
    ): Deferred<WeatherReport>


}


