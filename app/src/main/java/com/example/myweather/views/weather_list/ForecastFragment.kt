package com.example.myweather.views.weather_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myweather.R

class ForecastFragment : Fragment() {

    private lateinit var viewModel: ForecastViewModel
    private val forecastAdapter = ForecastListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_forecast, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<RecyclerView>(R.id.listForecast).apply {
            layoutManager = LinearLayoutManager(this@ForecastFragment.context)
            adapter = forecastAdapter
        }

        viewModel = ViewModelProvider(viewModelStore, ViewModelProvider.NewInstanceFactory())
            .get(ForecastViewModel::class.java)
        viewModel.liveData.observe(viewLifecycleOwner, { list ->
            forecastAdapter.listForecast = list
        })
        viewModel.fetchForecastData()
    }
}