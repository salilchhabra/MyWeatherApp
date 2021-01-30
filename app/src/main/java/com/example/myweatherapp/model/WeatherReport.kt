package com.example.myweatherapp.model

data class WeatherReport(
    val daily: List<Daily>,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Int
)


data class Daily(
    val dt: Long,
    val humidity: Int,
    val pressure: Int,
    val rain: Double?,
    val temp: Temp,
    val weather: List<WeatherDetails>,
    val wind_speed: Double
)


data class WeatherDetails(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)

data class FeelsLike(
    val day: Double,
    val eve: Double,
    val morn: Double,
    val night: Double
)

data class Temp(
    val day: Double,
    val eve: Double,
    val max: Double,
    val min: Double,
    val morn: Double,
    val night: Double
)


