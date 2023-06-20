package pl.inpost.recruitmenttask.util

import android.content.Context
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.util.Date
import java.util.Locale


object Utils {

    /**
     * Convert Date into String format
     */
    fun convertDate(dateToConvert: ZonedDateTime?): String? {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ROOT)
        val outputFormat = SimpleDateFormat("dd.MM.yyyy | HH:mm", Locale.ROOT)
        val date: Date? = inputFormat.parse(dateToConvert.toString())
        return date?.let { outputFormat.format(it) }
    }

    /**
     * Archive Shipments saves on SharedPreferences the Parcel Number of the Shipment to be hidden
     *
     * Once is saved is going to be always hidden (an alternative would be to hide or show between states)
     */
    fun archiveShipmentsOnSharedPreferences(context: Context, shipmentNumber: String) {
        val sharedPreference =  context.getSharedPreferences("PREFERENCE_NAME",Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putBoolean(shipmentNumber,true)
        editor.apply()
    }

    /**
     * Check if a particular Parcel Number is Archived or not
     */
    fun shipmentIsArchived(context: Context, shipmentNumber: String): Boolean {
        val sharedPreference =  context.getSharedPreferences("PREFERENCE_NAME",Context.MODE_PRIVATE)
        return sharedPreference.contains(shipmentNumber)
    }
}