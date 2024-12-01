package com.weatherassignment.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.weatherassignment.models.WeatherDetail

@Dao
interface WeatherDetailDao {
    /**
     * Duplicate values are replaced in the table.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addWeather(weatherDetail: WeatherDetail)

    @Query("SELECT * FROM ${WeatherDetail.TABLE_NAME} WHERE cityName = :cityName")
    suspend fun fetchWeatherByCity(cityName: String): WeatherDetail?
//ORDER BY Column_Primary_Key DESC"
    @Query("SELECT * FROM ${WeatherDetail.TABLE_NAME} ORDER BY dateTime DESC")
    suspend fun fetchAllWeatherDetails(): List<WeatherDetail>

}
