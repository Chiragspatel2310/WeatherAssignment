package com.weatherassignment.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.weatherassignment.models.WeatherDetail.Companion.TABLE_NAME

/**
 * Instantly see the temperature, condition (e.g., sunny, cloudy), and humidity.
 * Data class for Database entity and Serialization.
 */
@Entity(tableName = TABLE_NAME)
data class WeatherDetail(

    @PrimaryKey
    var id: Int? = 0,
    var temp: Double? = null,
    var icon: String? = null,
    var cityName: String? = null,
    var countryName: String? = null,
    var dateTime: String? = null,
    var humidity:Int?=0,
    var feelsLike: Double? = null,
    var speed: Double? = null
) {
    companion object {
        const val TABLE_NAME = "weather_detail"
    }
}