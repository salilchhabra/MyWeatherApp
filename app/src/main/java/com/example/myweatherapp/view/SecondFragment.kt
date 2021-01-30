package com.example.myweatherapp.view

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
import kotlinx.android.synthetic.main.fragment_second.*
import java.util.*


class SecondFragment : Fragment((R.layout.fragment_second)), AdapterView.OnItemSelectedListener {

    private lateinit var city: Array<String>
    private var viewModel: MainViewModel? = null
    private var mProgressDialog: CustomProgressDialog? = null
    var cityName: String? = null
    private var mSharedPreferenceUtils: SharedPreferenceUtils? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        mProgressDialog = CustomProgressDialog(activity)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSharedPreferenceUtils = activity?.applicationContext?.let {
            SharedPreferenceUtils.getInstance(
                it
            )
        }
        et_date.setOnClickListener {
            val cldr: Calendar = Calendar.getInstance()
            val day: Int = cldr.get(Calendar.DAY_OF_MONTH)
            val month: Int = cldr.get(Calendar.MONTH)
            val year: Int = cldr.get(Calendar.YEAR)
            // date picker dialog
            val datePickerDialog = activity?.let {
                DatePickerDialog(
                    it,
                    OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        et_date.setText(
                            dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
                        )

                    }, year, month, day
                )
            }

            datePickerDialog?.show()
        }
        city = arrayOf("Select city", "mumbai", "delhi", "amritsar", "ludhiana", "gurgaon")
        val adapt = activity?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, city) }
        adapt?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        with(spinner)
        {
            adapter = adapt
            setSelection(0, false)
            onItemSelectedListener = this@SecondFragment
        }
        btn_show.setOnClickListener {
            cityName?.let {
                mProgressDialog?.show("please wait")
                viewModel?.getCurrentWeather(cityName)
                observe()
            } ?: kotlin.run {
                Toast.makeText(activity, "select city and date", Toast.LENGTH_SHORT).show()
            }

        }
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
        data?.apply {
            iv_wind.visibility = View.VISIBLE
            tv_city.text = name
            mSharedPreferenceUtils?.let {
                if (it.containsKey("Celcius")) {
                    if (it.getSharedPrefBoolean("Celcius")!!) {
                        displayTempInCelcius(main)

                    } else {
                        displayTempInFeh(main)

                    }
                } else {
                    displayTempInCelcius(main)
                }
            }

            tv_condition.text = weather[0].main
            tv_wind.text = String.format("%s%s", wind.speed, "mph")
            tv_humidity.text = String.format("%s:%s%s", "Humidity", main.humidity, "%")
            tv_pressure.text = String.format("%s:%s%s", "Air Pressure", main.pressure, "hPa")


        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (position > 0) {
            cityName = city[position]
            Toast.makeText(context, city[position], Toast.LENGTH_LONG).show()
        }
    }

    private fun kelvinToCelcius(temp: Double) = run { temp.minus(273.15).toInt() }
    private fun kelvinToFeh(temp: Double) = run { (temp.minus(273.15) * 1.8 + 32).toInt() }

    private fun displayTempInCelcius(main: Main) {
        tv_temp.text = String.format("%s°C", kelvinToCelcius((main.temp)).toString())
        tv_max_min.text = String.format(
            "%s°C/%s°C",
            kelvinToCelcius((main.temp_max)).toString(),
            kelvinToCelcius((main.temp_min)).toString()
        )
    }

    private fun displayTempInFeh(main: Main) {
        tv_temp.text =
            String.format("%s°F", kelvinToFeh((main.temp)).toString())
        tv_max_min.text = String.format(
            "%s°F/%s°F",
            kelvinToFeh((main.temp_max)).toString(),
            kelvinToFeh((main.temp_min)).toString()
        )
    }

}