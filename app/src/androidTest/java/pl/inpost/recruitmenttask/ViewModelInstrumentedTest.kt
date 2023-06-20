package pl.inpost.recruitmenttask

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import pl.inpost.recruitmenttask.network.api.mockShipmentNetworkList
import pl.inpost.recruitmenttask.presentation.shipmentList.ShipmentListViewModel
import pl.inpost.recruitmenttask.presentation.shipmentList.adapters.Header

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ViewModelInstrumentedTest {
    private lateinit var appContext: Context

    @Test
    fun shipmentsListReturnsWithHeaders() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val viewModel = ShipmentListViewModel(null)
        val mockedShipmentsList = mockShipmentNetworkList()

        val listWithHeaders: MutableList<Any> = viewModel.getShipmentsListWithHeaders(appContext, mockedShipmentsList)
        var numberOfHeaders = 0

        for(i in listWithHeaders) {
            if(i is Header) {
                numberOfHeaders++
            }
        }

        assert(numberOfHeaders == 2)
    }


    @Test
    fun useAppContext() {
        // Context of the app under test.
        appContext = InstrumentationRegistry.getInstrumentation().targetContext

        Assert.assertEquals("pl.inpost.recruitmenttask", appContext.packageName)
    }
}