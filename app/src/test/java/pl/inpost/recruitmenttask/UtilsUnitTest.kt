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
import pl.inpost.recruitmenttask.network.api.mockShipmentNetwork
import pl.inpost.recruitmenttask.network.model.ShipmentNetwork
import pl.inpost.recruitmenttask.util.Utils
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class UtilsUnitTest {

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
    fun convertDateReturnsString() {
        val dateFromJSON = "2021-12-29T04:56:07Z"
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val dateTime = LocalDateTime.parse(dateFromJSON, formatter)
        val zonedDateTime = dateTime.atZone(ZoneId.of("Europe/London"))

        val mockShipmentNetwork: ShipmentNetwork = mockShipmentNetwork(expireDate = zonedDateTime)
        val convertedDate = Utils.convertDate(mockShipmentNetwork.expiryDate)

        val outputDate = "29.12.2021 | 04:56"

        Assert.assertEquals(outputDate, convertedDate)
    }
}