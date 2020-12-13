package com.example.myweather.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myweather.R
import com.example.myweather.databinding.FragmentFlowBinding
import com.example.myweather.model.db_service.ForecastDAO
import com.example.myweather.model.db_service.ForecastRoomDB
import com.example.myweather.views.today.TodayFragment
import com.example.myweather.views.weather_list.ForecastFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class FlowFragment : Fragment() {

    private lateinit var binding: FragmentFlowBinding
    private val todayFragment by lazy { TodayFragment() }
    private val forecastFragment by lazy { ForecastFragment() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_flow, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentFlowBinding.inflate(layoutInflater)

        childFragmentManager.beginTransaction()
            .add(binding.container.id, todayFragment)
            .add(binding.container.id, forecastFragment)
            .hide(forecastFragment)
            .commit()

        view.findViewById<BottomNavigationView>(R.id.navView)
            .setOnNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.navigation_list -> {
                        childFragmentManager.beginTransaction()
                            .show(todayFragment)
                            .hide(forecastFragment)
                            .commit()
                        return@setOnNavigationItemSelectedListener true
                    }
                    R.id.navigation_exchange -> {
                        childFragmentManager.beginTransaction()
                            .show(forecastFragment)
                            .hide(todayFragment)
                            .commit()
                        return@setOnNavigationItemSelectedListener true
                    }
                    else -> false
                }
            }
    }
}