package com.weatherassignment.data.remote

import com.shashank.weatherapp.data.network.NetworkState
import com.weatherassignment.models.WeatherDataResponse
import com.weatherassignment.utils.AppConstants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface RetrofitService {

    @GET("weather")
    suspend fun findCityWeatherData(
        @Query("q") q: String,
        @Query("units") units: String = AppConstants.WEATHER_UNIT,
        @Query("appid") appid: String = AppConstants.WEATHER_API_KEY
    ): Response<WeatherDataResponse>

    companion object {
        operator fun invoke(
            networkState: NetworkState
        ): RetrofitService {

            val okkHttpclient = OkHttpClient.Builder()
                .addInterceptor(networkState)
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .client(okkHttpclient)
                .baseUrl(AppConstants.WEATHER_API_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RetrofitService::class.java)
        }
    }
}
