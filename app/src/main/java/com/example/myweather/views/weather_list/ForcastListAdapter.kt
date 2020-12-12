package com.example.myweather.views.weather_list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myweather.R
import com.example.myweather.model.entity.ForecastForView
import kotlin.properties.Delegates

class ForecastListAdapter() :
    RecyclerView.Adapter<ForecastListAdapter.CryptocurrencyViewHolder>() {

    var listForecast: List<ForecastForView> by Delegates.observable(emptyList()) { _, oldValue, newValue ->
        notifyChanges(oldValue, newValue)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptocurrencyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CryptocurrencyViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: CryptocurrencyViewHolder, position: Int) {
        holder.bind(listForecast[position])
    }

    override fun getItemCount(): Int = listForecast.size

    class CryptocurrencyViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(
            inflater
                .inflate(R.layout.forecast_item, parent, false)
        ) {
        private var forecastImageView: ImageView? = null
        private var timeText: TextView? = null
        private var weatherDescriptionText: TextView? = null
        private var temperatureText: TextView? = null

        init {
            forecastImageView = itemView.findViewById(R.id.imageForecastItem)
            timeText = itemView.findViewById(R.id.timeForecastItemTextView)
            weatherDescriptionText = itemView.findViewById(R.id.weatherForecastItemTextView)
            temperatureText = itemView.findViewById(R.id.temperatureForecastItemTextView)
        }

        fun bind(forecast: ForecastForView) {
            forecastImageView?.setImageResource(R.drawable.ic_baseline_wb_cloudy_24)
            timeText?.text = forecast.date
            weatherDescriptionText?.text = forecast.weather
            temperatureText?.text = forecast.temperature
        }
    }

    private fun notifyChanges(oldList: List<ForecastForView>, newList: List<ForecastForView>) {
        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition].date == newList[newItemPosition].date
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition] == newList[newItemPosition]
            }

            override fun getOldListSize() = oldList.size
            override fun getNewListSize() = newList.size
        })
        diff.dispatchUpdatesTo(this)
    }
}