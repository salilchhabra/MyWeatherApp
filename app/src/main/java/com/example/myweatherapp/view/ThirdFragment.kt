package com.example.myweatherapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myweatherapp.R
import com.example.myweatherapp.adapter.WeatherAdapter
import com.example.myweatherapp.helper.CustomProgressDialog
import com.example.myweatherapp.helper.SharedPreferenceUtils
import com.example.myweatherapp.model.WeatherReport
import com.example.myweatherapp.viewModel.MainViewModel
import kotlinx.android.synthetic.main.fragment_third.*

class ThirdFragment : Fragment(R.layout.fragment_third) {
    private var viewModel: MainViewModel? = null
    private var mProgressDialog: CustomProgressDialog? = null
    private var weatherAdapter: WeatherAdapter? = null
    private var mSharedPreferenceUtils: SharedPreferenceUtils? = null

    companion object {
        private const val LATITUDE = "latitude"
        private const val LONGITUDE = "longitude"

        fun newInstance(latitude: String, longitude: String, userName: String?): ThirdFragment {
            val fragment = ThirdFragment()

            val bundle = Bundle().apply {
                putString(LATITUDE, latitude)
                putString(LONGITUDE, longitude)
            }
            fragment.arguments = bundle

            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val lat = arguments?.getString(LATITUDE)
        val lon = arguments?.getString(LONGITUDE)
        mSharedPreferenceUtils = activity?.applicationContext?.let {
            SharedPreferenceUtils.getInstance(
                it
            )
        }
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        mProgressDialog = CustomProgressDialog(activity)
        mProgressDialog?.show()
        viewModel?.getWeatherReport(lat, lon)
        observe()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun observe() {
        activity?.let {
            viewModel?.successReportLiveData?.observe(it,
                Observer {
                    mProgressDialog?.hide()
                    populateData(it)
                }
            )
        }
        activity?.let {
            viewModel?.errorLiveData?.observe(it,
                Observer {
                    mProgressDialog?.hide()
                    Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()

                }
            )
        }
    }

    private fun populateData(it: WeatherReport?) {
        weatherAdapter = it?.daily?.let { it1 -> WeatherAdapter(it1, mSharedPreferenceUtils) }
        rv_weather_list.apply {
            layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = weatherAdapter
            invalidate()
        }
    }

}