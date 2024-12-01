package com.weatherassignment.data.repositories

import com.weatherassignment.data.local.WeatherDatabase
import com.weatherassignment.data.remote.RetrofitService
import com.weatherassignment.data.remote.ApiClientRequest
import com.weatherassignment.models.WeatherDataResponse
import com.weatherassignment.models.WeatherDetail


class WeatherRepository(
    private val api: RetrofitService,
    private val db: WeatherDatabase
) : ApiClientRequest() {

    suspend fun findCityWeather(cityName: String): WeatherDataResponse = apiRequest {
        api.findCityWeatherData(cityName)
    }

    suspend fun addWeather(weatherDetail: WeatherDetail) {
        db.getWeatherDao().addWeather(weatherDetail)
    }

    suspend fun fetchWeatherDetail(cityName: String): WeatherDetail? =
        db.getWeatherDao().fetchWeatherByCity(cityName)

    suspend fun fetchAllWeatherDetails(): List<WeatherDetail> =
        db.getWeatherDao().fetchAllWeatherDetails()
}
