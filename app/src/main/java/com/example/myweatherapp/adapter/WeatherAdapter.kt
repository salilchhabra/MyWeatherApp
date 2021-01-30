package com.example.myweatherapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherapp.R
import com.example.myweatherapp.helper.SharedPreferenceUtils
import com.example.myweatherapp.model.Daily
import java.text.SimpleDateFormat
import java.util.*


class WeatherAdapter(
    private val data: List<Daily>,
    private val mSharedPreferenceUtils: SharedPreferenceUtils?
) :
    RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val degree: TextView = view.findViewById(R.id.tv_degree)
        val percentage: TextView = view.findViewById(R.id.tv_percent)
        val rainImage: ImageView = view.findViewById(R.id.iv_rain)
        val speed: TextView = view.findViewById(R.id.tv_speed)
        val date: TextView = view.findViewById(R.id.tv_time)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_weather, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        mSharedPreferenceUtils?.let {
            if (it.containsKey("Celcius")) {
                if (it.getSharedPrefBoolean("Celcius")!!) {
                    viewHolder.degree.text = String.format(
                        kelvinToCelcius(data[position].temp.day).toString() + "%s",
                        "°C"
                    )

                } else {
                    viewHolder.degree.text =
                        String.format(kelvinToFeh(data[position].temp.day).toString() + "%s", "°F")

                }
            } else {
                viewHolder.degree.text =
                    String.format(kelvinToCelcius(data[position].temp.day).toString() + "%s", "°C")
            }
        }
        viewHolder.date.text = convertTime(data[position].dt)
        viewHolder.percentage.text = data[position].rain?.let {
            viewHolder.rainImage.visibility = View.VISIBLE
            viewHolder.percentage.visibility = View.VISIBLE
            String.format(data[position].rain.toString() + "%s", "%")
        }
        viewHolder.speed.text = String.format(data[position].wind_speed.toString() + "%s", "mph")
    }

    override fun getItemCount() = data.size

    private fun convertTime(time: Long): String {
        return try {
            val netDate = Date(time * 1000)
            val sdf = SimpleDateFormat("MMMM d,yyyy ", Locale.ENGLISH)
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val formattedDate = sdf.format(netDate)
            formattedDate
        } catch (e: Exception) {
            e.toString()
        }
    }

    private fun kelvinToCelcius(temp: Double): Int = run { temp.minus(273.15).toInt() }
    private fun kelvinToFeh(temp: Double) = run { (temp.minus(273.15) * 1.8 + 32).toInt() }

}
