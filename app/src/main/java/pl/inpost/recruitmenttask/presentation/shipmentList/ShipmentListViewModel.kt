package pl.inpost.recruitmenttask.presentation.shipmentList

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.inpost.recruitmenttask.R
import pl.inpost.recruitmenttask.database.ShipmentNetworkDao
import pl.inpost.recruitmenttask.database.ShipmentNetworkRepository
import pl.inpost.recruitmenttask.database.ShipmentsDatabase
import pl.inpost.recruitmenttask.network.api.ShipmentApi
import pl.inpost.recruitmenttask.network.model.ShipmentNetwork
import pl.inpost.recruitmenttask.presentation.shipmentList.adapters.Header
import pl.inpost.recruitmenttask.util.setState
import javax.inject.Inject

@HiltViewModel
class ShipmentListViewModel @Inject constructor(
    private val shipmentApi: ShipmentApi?
) : ViewModel() {

    private val mutableViewState = MutableLiveData<List<ShipmentNetwork>>(emptyList())
    val viewState: LiveData<List<ShipmentNetwork>> = mutableViewState
    private var shipmentsByStatusList : MutableList<Any> = ArrayList()
    private var readyToPickupShipmentsList : MutableList<ShipmentNetwork> = ArrayList()
    private var remainingShipmentsList : MutableList<ShipmentNetwork> = ArrayList()
    private var shipmentsByParcelNumberList : MutableList<ShipmentNetwork> = ArrayList()
    private var shipmentsByPickupDateList : MutableList<ShipmentNetwork> = ArrayList()
    private var shipmentsByExpireDateList : MutableList<ShipmentNetwork> = ArrayList()
    private var shipmentsByStoredDateList : MutableList<ShipmentNetwork> = ArrayList()
    private lateinit var shipmentsDatabase : ShipmentsDatabase
    private lateinit var shipmentNetworkRepository : ShipmentNetworkRepository
    private lateinit var shipmentNetworkDao: ShipmentNetworkDao

    fun setShipmentsDatabase(shipmentsDatabase : ShipmentsDatabase) {
        this.shipmentsDatabase = shipmentsDatabase
        shipmentNetworkDao = shipmentsDatabase.shipmentNetworkDao()
        shipmentNetworkRepository = ShipmentNetworkRepository(shipmentNetworkDao)
    }

    /**
     * Insert Shipment into Room Database
     */
    private fun insert(shipmentNetwork: ShipmentNetwork) = viewModelScope.launch {
        launch(Dispatchers.IO) {
            shipmentNetworkRepository.insert(shipmentNetwork)
        }
    }

    init {
        refreshData()
    }

    /**
     * Get Shipments from Service
     */
    fun refreshData() {
        viewModelScope.launch(Dispatchers.Main) {
            if (shipmentApi != null) {
                val shipments = shipmentApi.getShipments()
                mutableViewState.setState { shipments }

                for (i in shipments ){
                    insert(i)
                }
            }
        }
    }

    /**
     * Add the Headers for the List
     * - Ready for Pick up
     * - Remaining
     */
    fun getShipmentsListWithHeaders(context: Context, shipments: List<ShipmentNetwork>): MutableList<Any> {
        arrangeListsForFilter(shipments)
        shipmentsByStatusList = ArrayList()
        shipmentsByStatusList.add(Header(context.getString(R.string.status_ready_to_pickup)))
        shipmentsByStatusList.addAll(readyToPickupShipmentsList)
        shipmentsByStatusList.add(Header(context.getString(R.string.the_remaining)))
        shipmentsByStatusList.addAll(remainingShipmentsList)
        return shipmentsByStatusList
    }

    /**
     * Store all different lists in order to filter it on Home Screen
     */
    internal fun arrangeListsForFilter(shipments: List<ShipmentNetwork>) {
        readyToPickupShipmentsList = shipments.filter { it.operations.highlight }.toMutableList()
        remainingShipmentsList = shipments.filter { !it.operations.highlight }.toMutableList()
        shipmentsByParcelNumberList = shipments.sortedByDescending { it.number }.toMutableList()
        shipmentsByPickupDateList = shipments.sortedByDescending { it.pickUpDate }.toMutableList()
        shipmentsByExpireDateList = shipments.sortedByDescending { it.expiryDate }.toMutableList()
        shipmentsByStoredDateList = shipments.sortedByDescending { it.storedDate }.toMutableList()
    }

    /**
     * Returns the list of Ready to Pick Up Shipments
     */
    fun getReadyShipments(): MutableList<ShipmentNetwork> {
        return readyToPickupShipmentsList
    }

    /**
     * Returns the list of Remaining Shipments
     */
    fun getRemainingShipments(): MutableList<ShipmentNetwork> {
        return remainingShipmentsList
    }

    /**
     * Returns the Shipments by Parcel Number Descending
     */
    fun getShipmentsByParcelNumber(): MutableList<ShipmentNetwork> {
        return shipmentsByParcelNumberList
    }

    /**
     * Returns the Shipments by Pickup Date Descending
     */
    fun getShipmentsByPickupDate(): MutableList<ShipmentNetwork> {
        return shipmentsByPickupDateList
    }

    /**
     * Returns the Shipments by Expire Date Descending
     */
    fun getShipmentsByExpireDate(): MutableList<ShipmentNetwork> {
        return shipmentsByExpireDateList
    }

    /**
     * Returns the Shipments by Stored Date Descending
     */
    fun getShipmentsByStoredDate(): MutableList<ShipmentNetwork> {
        return shipmentsByStoredDateList
    }
}
