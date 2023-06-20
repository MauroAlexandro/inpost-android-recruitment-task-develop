package pl.inpost.recruitmenttask.database

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import pl.inpost.recruitmenttask.network.model.ShipmentNetwork

class ShipmentNetworkRepository (private val shipmentNetworkDao: ShipmentNetworkDao) {

    val allShipmentNetwork: Flow<MutableList<ShipmentNetwork>> = shipmentNetworkDao.getAllShipmentNetwork()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(shipmentNetwork: ShipmentNetwork){
        shipmentNetworkDao.insert(shipmentNetwork)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(shipmentNetwork: ShipmentNetwork){
        shipmentNetworkDao.delete(shipmentNetwork)
    }

}