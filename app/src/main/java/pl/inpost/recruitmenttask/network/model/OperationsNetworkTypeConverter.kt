package pl.inpost.recruitmenttask.network.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class OperationsNetworkTypeConverter {
    private val gson: Gson = Gson()
    private val type: Type = object : TypeToken<OperationsNetwork?>() {}.type
    @TypeConverter
    fun stringToOperationsNetwork(json: String?): OperationsNetwork? {
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun operationsNetworkToString(operationsNetwork: OperationsNetwork?): String {
        return gson.toJson(operationsNetwork, type)
    }
}