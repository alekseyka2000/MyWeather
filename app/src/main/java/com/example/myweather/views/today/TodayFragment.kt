package com.example.myweather.views.today

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myweather.databinding.FragmentTodayBinding
import com.example.myweather.model.entity.ForecastForView
import com.example.myweather.views.weather_list.ForecastViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension


@KoinApiExtension
@Suppress("DEPRECATION")
class TodayFragment : Fragment() {

    //didn't create viewModel for this fragment so ForecastViewModel is enough
    private val viewModel: ForecastViewModel by viewModel()
    private lateinit var binding: FragmentTodayBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTodayBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.liveData.observe(viewLifecycleOwner, { list ->
            setTodayFragment(list.second)
            Snackbar.make(view, list.first, Snackbar.LENGTH_SHORT).show()
        })
        viewModel.fetchForecastData()
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    fun setTodayFragment(forecast: List<ForecastForView>) {

        //set weather image
        binding.weatherImage.setImageDrawable(
            resources.getDrawable(
                resources
                    .getIdentifier(("pic" + forecast[0].imageURL), "drawable", context?.packageName)
            )
        )

        //set city text
        val country = if (forecast[0].country == "none") "" else (", " + forecast[0].country)
        binding.location.text = (forecast[0].city + country)

        //set temperature text
        binding.temperature.text = (forecast[0].temperature + " ℃ | "
                + forecast[0].weather)

        //set humidity text
        binding.humidity.text = (forecast[0].humidity + " %")

        //set precipitation text
        //used API is'n share precipitation value/ so will be use default value 1 !!!!!
        binding.precipitation.text = forecast[0].precipitation

        //set pressure text
        // incorrect icon for that value!!!!!!!!!!
        binding.pressure.text = "${forecast[0].pressure} hPa"

        //set wind speed text
        binding.windSpeed.text = "${forecast[0].windSpeed} km/h"

        //set wind direction text
        binding.windDeg.text = forecast[0].directionOfTheWind
    }

    override fun onStart() {
        super.onStart()
        binding.shareButton.setOnClickListener {
            val intent = Intent()
            val textMessage: String = "Today in ${binding.location.text}" +
                    " (temperature = ${binding.temperature.text}" +
                    ", humidity = ${binding.humidity.text}" +
                    ", precipitation = ${binding.precipitation.text}" +
                    ", pressure = ${binding.pressure.text}" +
                    ", wind speed = ${binding.windSpeed.text}" +
                    ", wind direction = ${binding.windDeg.text})"
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, textMessage)
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, "Share today forecast: "))
        }
    }

    fun reset(){
        viewModel.fetchForecastData()
    }
}