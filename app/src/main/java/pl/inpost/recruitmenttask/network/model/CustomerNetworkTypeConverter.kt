package pl.inpost.recruitmenttask.network.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class CustomerNetworkTypeConverter {
    private val gson: Gson = Gson()
    private val type: Type = object : TypeToken<CustomerNetwork?>() {}.type
    @TypeConverter
    fun stringToCustomerNetwork(json: String?): CustomerNetwork? {
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun customerNetworkToString(customerNetwork: CustomerNetwork?): String {
        return gson.toJson(customerNetwork, type)
    }
}