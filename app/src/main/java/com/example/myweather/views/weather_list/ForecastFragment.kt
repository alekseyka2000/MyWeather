package com.example.myweather.views.weather_list

import android.annotation.SuppressLint
import android.os.Build
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
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
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
            val newList = mutableListOf<Pair<Int, ForecastForView>>()
            list.second.forEach {
                if (it.date.takeLast(8) == "00:00:00") {
                    newList.add(Pair(1, it))
                    newList.add(Pair(0, it))
                } else newList.add(Pair(0, it))
            }
            forecastAdapter.listForecast = newList
            Snackbar.make(view, list.first, Snackbar.LENGTH_LONG).show()
        })
        viewModel.fetchForecastData()
    }

    inner class ForecastListAdapter() :
        RecyclerView.Adapter<ForecastListAdapter.BaseViewHolder>() {

        var listForecast: List<Pair<Int, ForecastForView>> by Delegates.observable(emptyList()) { _, oldValue, newValue ->
            notifyChanges(oldValue, newValue)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return if (viewType == 1) DayViewHolder(inflater, parent) else WeatherViewHolder(inflater, parent)
        }

        override fun getItemViewType(position: Int) = listForecast[position].first

        override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
            holder.bind(listForecast[position].second)
        }

        override fun getItemCount(): Int = listForecast.size

        open inner class BaseViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
            open fun bind(forecast: ForecastForView){}
        }

        inner class DayViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
            BaseViewHolder(inflater.inflate(R.layout.day_of_week_item, parent, false)){
            private var dayOfWeekTextView: TextView? = null

            init {
                dayOfWeekTextView = itemView.findViewById(R.id.dayOfWeek)
            }

            @SuppressLint("SimpleDateFormat", "SetTextI18n")
            override fun bind (forecast: ForecastForView){
                val year: Int  = forecast.date.take(4).toInt()
                val month: Int  = forecast.date.take(7).takeLast(2).toInt()
                val day: Int  = forecast.date.take(10).takeLast(2).toInt()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    dayOfWeekTextView?.text = LocalDate.parse(forecast.date.take(10)).dayOfWeek.toString()
                }else dayOfWeekTextView?.text = SimpleDateFormat("EEEE")
                    .format(Date(year, month, day)).capitalize(Locale.getDefault())
            }
        }

        inner class WeatherViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
            BaseViewHolder(inflater.inflate(R.layout.forecast_item, parent, false)) {
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
            override fun bind(forecast: ForecastForView) {
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

        private fun notifyChanges(
            oldList: List<Pair<Int, ForecastForView>>,
            newList: List<Pair<Int, ForecastForView>>
        ) {
            val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return oldList[oldItemPosition].second.date == newList[newItemPosition].second.date
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