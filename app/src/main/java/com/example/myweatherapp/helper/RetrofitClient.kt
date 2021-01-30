package com.example.myweatherapp.helper

import com.example.myweatherapp.service.ApiInterface
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private var BASE_URL = "https://api.openweathermap.org/data/2.5/"

    private fun getRetrofitBuilder() =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(OkHttpClient())
            .build()

    fun getApi(): ApiInterface? = getRetrofitBuilder().create(ApiInterface::class.java)
}