package com.example.myweather.views.weather_list

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myweather.R
import com.example.myweather.model.entity.ForecastForView
import com.google.android.material.snackbar.Snackbar
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension
import kotlin.properties.Delegates

@KoinApiExtension
@Suppress("DEPRECATION")
class ForecastFragment : Fragment() {

    private val viewModel: ForecastViewModel by viewModel()
    private val forecastAdapter = ForecastListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_forecast, container, false)
    }

    @KoinApiExtension
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<RecyclerView>(R.id.listForecast).apply {
            layoutManager = LinearLayoutManager(this@ForecastFragment.context)
            adapter = forecastAdapter
        }

        viewModel.liveData.observe(viewLifecycleOwner, { list ->
            forecastAdapter.listForecast = list.second
            Snackbar.make(view, list.first, Snackbar.LENGTH_LONG).show()
        })
        viewModel.fetchForecastData()
    }

    inner class ForecastListAdapter() :
        RecyclerView.Adapter<ForecastListAdapter.WeatherViewHolder>() {

        var listForecast: List<ForecastForView> by Delegates.observable(emptyList()) { _, oldValue, newValue ->
            notifyChanges(oldValue, newValue)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return WeatherViewHolder(inflater, parent)
        }

        override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
            holder.bind(listForecast[position])
        }

        override fun getItemCount(): Int = listForecast.size

        inner class WeatherViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
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

            @SuppressLint("UseCompatLoadingForDrawables")
            fun bind(forecast: ForecastForView) {
                forecastImageView?.setImageDrawable(
                    resources.getDrawable(
                        resources
                            .getIdentifier(
                                ("pic" + forecast.imageURL),
                                "drawable",
                                context?.packageName
                            )
                    )
                )
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

                override fun areContentsTheSame(
                    oldItemPosition: Int,
                    newItemPosition: Int
                ): Boolean {
                    return oldList[oldItemPosition] == newList[newItemPosition]
                }

                override fun getOldListSize() = oldList.size
                override fun getNewListSize() = newList.size
            })
            diff.dispatchUpdatesTo(this)
        }
    }
}