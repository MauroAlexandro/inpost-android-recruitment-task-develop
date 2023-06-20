package pl.inpost.recruitmenttask

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import pl.inpost.recruitmenttask.util.Utils

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class UtilsInstrumentedTest {
    private lateinit var appContext: Context

    @Test
    fun checkArchivingOfParcelNumber() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val parcelNumber = "000000001"
        Utils.archiveShipmentsOnSharedPreferences(appContext, parcelNumber)
        val isShipmentNumberArchived = Utils.shipmentIsArchived(appContext, parcelNumber)

        assertTrue(isShipmentNumberArchived)
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        appContext = InstrumentationRegistry.getInstrumentation().targetContext

        assertEquals("pl.inpost.recruitmenttask", appContext.packageName)
    }
}