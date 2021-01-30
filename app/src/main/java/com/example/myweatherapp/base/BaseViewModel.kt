package com.example.myweatherapp.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

abstract class BaseViewModel : ViewModel() {

    private val job: Job = Job()

    val viewModelScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCleared() {
        job.cancel()
        super.onCleared()
    }
}