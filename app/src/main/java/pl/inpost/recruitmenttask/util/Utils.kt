package pl.inpost.recruitmenttask.util

import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.util.Date
import java.util.Locale


object Utils {

    fun convertDate(dateToConvert: ZonedDateTime?): String? {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ROOT)
        val outputFormat = SimpleDateFormat("dd.MM.yyyy | HH:mm", Locale.ROOT)
        val date: Date? = inputFormat.parse(dateToConvert.toString())
        return date?.let { outputFormat.format(it) }
    }
}