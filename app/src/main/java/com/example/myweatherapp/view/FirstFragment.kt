package com.example.myweatherapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myweatherapp.R
import com.example.myweatherapp.helper.CustomProgressDialog
import com.example.myweatherapp.helper.SharedPreferenceUtils
import com.example.myweatherapp.model.Main
import com.example.myweatherapp.model.WeatherModel
import com.example.myweatherapp.viewModel.MainViewModel
import kotlinx.android.synthetic.main.fragment_first.*

class FirstFragment : Fragment((R.layout.fragment_first)) {
    private var viewModel: MainViewModel? = null
    private var mProgressDialog: CustomProgressDialog? = null
    private var userName: String? = null
    private var mSharedPreferenceUtils: SharedPreferenceUtils? = null

    companion object {
        const val CITY_NAME = "city_name"
        const val USER_NAME = "user_name"

        fun newInstance(name: String, userName: String?): FirstFragment {
            val fragment = FirstFragment()

            val bundle = Bundle().apply {
                putString(CITY_NAME, name)
                putString(USER_NAME, userName)
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
        val name = arguments?.getString(CITY_NAME)
        userName = arguments?.getString(USER_NAME)
        mSharedPreferenceUtils = activity?.applicationContext?.let {
            SharedPreferenceUtils.getInstance(
                it
            )
        }
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        mProgressDialog = CustomProgressDialog(activity)
        mProgressDialog?.show()
        viewModel?.getCurrentWeather(name)
        observe()

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun observe() {
        activity?.let {
            viewModel?.successLiveData?.observe(it,
                Observer {
                    mProgressDialog?.hide()
                    updateUi(it)
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

    private fun updateUi(data: WeatherModel?) {
        tv_name.text = String.format("welcome %s", userName)
        data?.apply {
            iv_wind.visibility = View.VISIBLE
            tv_city.text = name
            mSharedPreferenceUtils?.let {
                if (it.containsKey("Celcius")) {
                    if (it.getSharedPrefBoolean("Celcius")!!) {
                        displaytempInCelcius(main)

                    } else {
                        displaytempInFeh(main)

                    }
                } else {
                    displaytempInCelcius(main)
                }
            }

            tv_condition.text = weather[0].main
            tv_wind.text = String.format("%s%s", wind.speed, "mph")
            tv_humidity.text = String.format("%s:%s%s", "Humidity", main.humidity, "%")
            tv_pressure.text = String.format("%s:%s%s", "Air Pressure", main.pressure, "hPa")


        }
    }

    private fun kelvinToCelcius(temp: Double) = run { temp.minus(273.15) }
    private fun kelvinToFeh(temp: Double) = run { temp.minus(273.15) * 1.8 + 32 }

    private fun displaytempInCelcius(main: Main) {
        tv_temp.text = String.format("%s°C", kelvinToCelcius((main.temp)).toString())
        tv_max_min.text = String.format(
            "%s°C/%s°C",
            kelvinToCelcius((main.temp_max)).toString(),
            kelvinToCelcius((main.temp_min)).toString()
        )
    }

    private fun displaytempInFeh(main: Main) {
        tv_temp.text =
            String.format("%s°F", kelvinToFeh((main.temp)).toString())
        tv_max_min.text = String.format(
            "%s°F/%s°F",
            kelvinToFeh((main.temp_max)).toString(),
            kelvinToFeh((main.temp_min)).toString()
        )
    }

}