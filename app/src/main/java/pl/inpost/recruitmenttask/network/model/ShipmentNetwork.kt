package pl.inpost.recruitmenttask.network.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.time.ZonedDateTime
@Entity(tableName = "shipments_table")
data class ShipmentNetwork(
    @PrimaryKey(autoGenerate = false) val number: String,
    val shipmentType: String,
    val status: ShipmentStatus,
    @TypeConverters(EventLogNetworkTypeConverter::class)
    val eventLog: List<EventLogNetwork>,
    val openCode: String?,
    @TypeConverters(ZonedDateTimeTypeConverter::class)
    val expiryDate: ZonedDateTime?,
    @TypeConverters(ZonedDateTimeTypeConverter::class)
    val storedDate: ZonedDateTime?,
    @TypeConverters(ZonedDateTimeTypeConverter::class)
    val pickUpDate: ZonedDateTime?,
    @TypeConverters(CustomerNetworkTypeConverter::class)
    val receiver: CustomerNetwork?,
    @TypeConverters(CustomerNetworkTypeConverter::class)
    val sender: CustomerNetwork?,
    @TypeConverters(OperationsNetworkTypeConverter::class)
    val operations: OperationsNetwork
)
