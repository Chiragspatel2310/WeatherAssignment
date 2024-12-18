package com.weatherassignment.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date


object AppUtils {

    fun showProgressBar(requireContext: Context) {
        if (!ProgressBar.getInstance().isDialogShowing()) {
            ProgressBar.getInstance().showProgress(requireContext, false)
        }
    }

    fun hideProgressBar() {
        ProgressBar.getInstance().dismissProgress()
    }

    fun setGlideImage(image: ImageView, url: String) {

        Glide.with(image).load(url)
            .thumbnail(0.5f)
            .into(image)

    }

    @SuppressLint("SimpleDateFormat")
    fun getCurrentDateTime(dateFormat: String): String =
        SimpleDateFormat(dateFormat).format(Date())

    @SuppressLint("SimpleDateFormat")
    fun isTimeExpired(dateTimeSavedWeather: String?): Boolean {
        dateTimeSavedWeather?.let {
            val currentDateTime = Date()
            val savedWeatherDateTime =
                SimpleDateFormat(AppConstants.DATE_FORMAT_1).parse(it)
            val diff: Long = currentDateTime.time - savedWeatherDateTime.time
            val seconds = diff / 1000
            val minutes = seconds / 60
            if (minutes > 10)
                return true
        }
        return false
    }

    fun parseDate(
        inputDateString: String?
    ): String? {
        var date: Date? = null
        var outputDateString: String? = null
        try {
            val inputDateFormat = SimpleDateFormat(AppConstants.DATE_FORMAT_1)
            val outputDateFormat = SimpleDateFormat(AppConstants.DATE_FORMAT)
            date = inputDateFormat.parse(inputDateString)
            outputDateString = outputDateFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return outputDateString
    }
    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
    }
}