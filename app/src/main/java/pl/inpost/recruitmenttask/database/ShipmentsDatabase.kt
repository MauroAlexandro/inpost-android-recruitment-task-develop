package pl.inpost.recruitmenttask.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import pl.inpost.recruitmenttask.network.model.CustomerNetworkTypeConverter
import pl.inpost.recruitmenttask.network.model.EventLogNetworkTypeConverter
import pl.inpost.recruitmenttask.network.model.OperationsNetworkTypeConverter
import pl.inpost.recruitmenttask.network.model.ShipmentNetwork
import pl.inpost.recruitmenttask.network.model.ZonedDateTimeTypeConverter

@Database(entities = [ShipmentNetwork::class], version = 1, exportSchema = false)
@TypeConverters(
    EventLogNetworkTypeConverter::class,
    ZonedDateTimeTypeConverter::class,
    CustomerNetworkTypeConverter::class,
    OperationsNetworkTypeConverter::class
)
abstract class ShipmentsDatabase: RoomDatabase() {

    abstract fun shipmentNetworkDao(): ShipmentNetworkDao

    companion object {
        private var instance: ShipmentsDatabase? = null

        @Synchronized
        fun getInstance(ctx: Context): ShipmentsDatabase {
            if(instance == null)
                instance = Room.databaseBuilder(ctx.applicationContext, ShipmentsDatabase::class.java,
                    "note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build()

            return instance!!

        }

        private val roomCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                populateDatabase(instance!!)
            }
        }

        // We could populate the database when RoomDatabase is created
        private fun populateDatabase(db: ShipmentsDatabase) {
            /*
            val shipmentNetworkDao = db.shipmentNetworkDao()
            GlobalScope.launch {
                shipmentNetworkDao.insert(ShipmentNetwork("..."))

            }*/
        }
    }
}