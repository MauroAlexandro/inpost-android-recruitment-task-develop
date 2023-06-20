package pl.inpost.recruitmenttask

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import pl.inpost.recruitmenttask.network.api.mockShipmentNetworkList
import pl.inpost.recruitmenttask.presentation.shipmentList.ShipmentListViewModel

class ViewModelUnitTest {

    @OptIn(DelicateCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun givenTwoParcelNumbersReturnsInOrder () {
        val viewModel = ShipmentListViewModel(null)
        val mockedShipmentsList = mockShipmentNetworkList()

        viewModel.arrangeListsForFilter(mockedShipmentsList)

        val orderedByParcelNumber = viewModel.getShipmentsByParcelNumber()
        val firstItem = orderedByParcelNumber[0]
        val firstItemParcelNumber = "0000002"

        Assert.assertEquals(firstItem.number, firstItemParcelNumber)
    }

    @Test
    fun readyToPickupShipmentsListReturnsOnlyOne() {
        //Giving two Parcels when only one is ready, It only should return one Parcel that's ready
        val viewModel = ShipmentListViewModel(null)
        val mockedShipmentsList = mockShipmentNetworkList()

        viewModel.arrangeListsForFilter(mockedShipmentsList)
        val parcelsReady = viewModel.getReadyShipments()

        Assert.assertTrue(parcelsReady.size == 1)
    }
}