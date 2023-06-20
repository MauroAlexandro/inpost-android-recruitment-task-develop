package pl.inpost.recruitmenttask.network.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class EventLogNetworkTypeConverter {
    private val gson: Gson = Gson()
    private val type: Type = object : TypeToken<List<EventLogNetwork?>?>() {}.type
    @TypeConverter
    fun stringToEventLogNetwork(json: String?): List<EventLogNetwork>? {
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun eventLogNetworkToString(eventLogNetwork: List<EventLogNetwork?>?): String {
        return gson.toJson(eventLogNetwork, type)
    }
}