package pl.inpost.recruitmenttask.network.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.time.ZonedDateTime

class ZonedDateTimeTypeConverter {
    private val gson: Gson = Gson()
    private val type: Type = object : TypeToken<ZonedDateTime?>() {}.type
    @TypeConverter
    fun stringToZonedDateTime(json: String?): ZonedDateTime? {
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun zonedDateTimeToString(zonedDateTime: ZonedDateTime?): String {
        return gson.toJson(zonedDateTime, type)
    }
}