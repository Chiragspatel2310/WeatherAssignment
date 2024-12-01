package com.weatherassignment.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.weatherassignment.R
import com.weatherassignment.databinding.ActivityWeatherBinding
import com.weatherassignment.models.WeatherDetail
import com.weatherassignment.ui.adapters.SearchedCityTemperatureAdapter
import com.weatherassignment.ui.viewmodel.WeatherViewModel
import com.weatherassignment.ui.viewmodelfactory.WeatherViewModelFactory
import com.weatherassignment.utils.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance


class WeatherActivity : AppCompatActivity(), KodeinAware {
    override val kodein by closestKodein()
    private lateinit var dataBind: ActivityWeatherBinding
    private lateinit var searchedCityTemperatureAdapter: SearchedCityTemperatureAdapter

    private val factory: WeatherViewModelFactory by instance()
    private val viewModel: WeatherViewModel by lazy {
        ViewModelProvider(this, factory).get(WeatherViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBind = DataBindingUtil.setContentView(this, R.layout.activity_weather)
        setupUI()
        observeAPICall()
        viewModel.fetchAllWeatherDetailsFromDb()
    }

    private fun setupUI() {
        initializeRecyclerView()
        dataBind.inputFindCityWeather.setOnEditorActionListener { view, actionId, event ->
            if (dataBind.inputFindCityWeather.text.toString().isEmpty()) {
                AppUtils.hideKeyboard(this)
                dataBind.root.snackbar(getString(R.string.city_error))
            } else {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    viewModel.fetchWeatherDetailFromDb((view as EditText).text.toString())
                }
            }
            false
        }
        dataBind.imageSearch.setOnClickListener {
            AppUtils.hideKeyboard(this)
            if (dataBind.inputFindCityWeather.text.toString().isEmpty()) {
                dataBind.root.snackbar(getString(R.string.city_error))
            } else {
                viewModel.fetchWeatherDetailFromDb(dataBind.inputFindCityWeather.text.toString())
            }
        }
    }

    private fun initializeRecyclerView() {
        searchedCityTemperatureAdapter = SearchedCityTemperatureAdapter(itemClickListner = {
            showData(it)
        })
        val mLayoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        dataBind.recyclerViewSearchedCityTemperature.apply {
            layoutManager = mLayoutManager
            itemAnimator = DefaultItemAnimator()
            adapter = searchedCityTemperatureAdapter
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observeAPICall() {
        viewModel.weatherLiveData.observe(this, EventObserver { state ->
            when (state) {
                is State.Loading -> {
                }

                is State.Success -> {

                    viewVisible()
                    dataBind.inputFindCityWeather.text?.clear()
                    state.data.let { weatherDetail ->
                        viewModel.fetchAllWeatherDetailsFromDb()
                    }
                }

                is State.Error -> {
//                    showToast(state.message)
                    if (dataBind.recyclerViewSearchedCityTemperature.adapter?.itemCount == 0)
                        viewGone()

                    dataBind.root.snackbar(state.message)
                }
            }
        })

        viewModel.weatherDetailListLiveData.observe(this, EventObserver { state ->
            when (state) {
                is State.Loading -> {
                }

                is State.Success -> {
                    if (state.data.isEmpty()) {
                        viewGone()
                        dataBind.recyclerViewSearchedCityTemperature.hide()
                    } else {
                        showData(state.data.get(0))
                        viewVisible()
                        dataBind.recyclerViewSearchedCityTemperature.show()
                        searchedCityTemperatureAdapter.setData(state.data)
                    }
                }

                is State.Error -> {
                    viewGone()
                    dataBind.root.snackbar(state.message)
                }
            }
        })
    }

    private fun changeBgAccToTemp(iconCode: String?) {
        //sunny, rainy, windy, stormy, and cloudy
        when (iconCode) {
            "01d", "02d", "03d" -> dataBind.weatherCardView.currentImage.setImageResource(R.drawable.sunny_day)
            "04d", "09d", "10d", "11d" -> dataBind.weatherCardView.currentImage.setImageResource(R.drawable.raining)
            "13d", "50d" -> dataBind.weatherCardView.currentImage.setImageResource(R.drawable.snowfalling)
        }
    }

    private fun viewVisible() {
        dataBind.textLabelSearchForCity.hide()
        dataBind.imageCity.hide()
        dataBind.constraintLayoutShowingTemp.show()
        dataBind.weatherCardView.root.show()
        dataBind.textCityName.show()
        dataBind.textTodaysDate.show()
        dataBind.textLabelToday.show()
    }

    private fun viewGone() {
        dataBind.textLabelSearchForCity.show()
        dataBind.imageCity.show()
        dataBind.constraintLayoutShowingTemp.hide()
        dataBind.weatherCardView.root.hide()
        dataBind.textCityName.hide()
        dataBind.textTodaysDate.hide()
        dataBind.textLabelToday.hide()
    }

    private fun showData(weatherDetail: WeatherDetail) {
        val iconCode = weatherDetail.icon?.replace("n", "d")
        changeBgAccToTemp(iconCode)
        dataBind.textTodaysDate.text = AppUtils.parseDate(weatherDetail.dateTime)
//            AppUtils.getCurrentDateTime(AppConstants.DATE_FORMAT)
        dataBind.weatherCardView.textTemperature.text =
            weatherDetail.temp.toString()
        dataBind.textCityName.text =
            "${weatherDetail.cityName?.capitalize()}, ${weatherDetail.countryName}"
        dataBind.weatherCardView.textHumidityPercentage.text =
            getString(R.string.humidity_template, weatherDetail.humidity)
        dataBind.weatherCardView.textTempFeelsLike.text =
            getString(R.string.temperature, weatherDetail.feelsLike?.toInt())
        dataBind.weatherCardView.textWindVelocity.text =
            getString(R.string.wind_speed, weatherDetail.speed?.toInt())
    }
}