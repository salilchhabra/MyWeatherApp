package com.example.myweatherapp.helper

import android.content.Context
import com.example.myweatherapp.helper.Constants.PREF_WEATHER

class SharedPreferenceUtils(context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )

    private val editor = sharedPreferences?.edit()

    companion object {
        private const val PREFS_NAME = PREF_WEATHER

        @Volatile
        private var instance: SharedPreferenceUtils? = null

        fun getInstance(context: Context): SharedPreferenceUtils? {
            if (instance == null) {
                instance = SharedPreferenceUtils(context)
            }
            return instance
        }
    }

    fun containsKey(key: String?) = sharedPreferences.contains(key)


    fun putSharedPref(key: String?, value: String?) {
        sharedPreferences.edit().putString(key, value).commit()
    }

    fun getSharedPref(key: String?): String? {
        return sharedPreferences.getString(key, null)
    }

    fun putSharedPrefBoolean(key: String?, value: Boolean?) {
        sharedPreferences.edit().putBoolean(key, value!!).commit()
    }

    fun getSharedPrefBoolean(key: String?): Boolean? {
        return sharedPreferences.getBoolean(key, false)
    }
}