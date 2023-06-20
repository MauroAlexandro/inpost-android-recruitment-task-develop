package pl.inpost.recruitmenttask.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import pl.inpost.recruitmenttask.network.model.ShipmentNetwork

@Dao
interface ShipmentNetworkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(shipmentNetwork: ShipmentNetwork)

    @Update
    fun update(shipmentNetwork: ShipmentNetwork)

    @Delete
    fun delete(shipmentNetwork: ShipmentNetwork)

    @Query("delete from shipments_table")
    fun deleteAllShipmentNetwork()

    @Query("select * from shipments_table")
    fun getAllShipmentNetwork(): Flow<MutableList<ShipmentNetwork>>
}