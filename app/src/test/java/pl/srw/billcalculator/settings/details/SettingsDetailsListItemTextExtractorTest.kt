package pl.srw.billcalculator.settings.details

import android.content.Context
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Assert.assertEquals
import org.junit.Test
import pl.srw.billcalculator.R

class SettingsDetailsListItemTextExtractorTest {

    val context: Context = mock {
        on { getString(R.string.price_disabled) } doReturn "Not included"
        on { getString(1) } doReturn "kWh"
        on { getString(2) } doReturn "option1"
        on { getString(3) } doReturn "tit2"
    }
    val tested = SettingsDetailsListItemTextExtractor

    @Test
    fun `given input entry return direct title`() {
        val title = "tit1"
        val item = InputSettingsDetailsListItem(title, true, true, "1.00", 1, 2)

        val result = tested.getTitle(context, item)

        assertEquals(title, result)
    }

    @Test
    fun `given picking entry return resource title`() {
        val item = PickingSettingsDetailsListItem(3, 2, emptyList())

        val result = tested.getTitle(context, item)

        assertEquals("tit2", result)
    }

    @Test
    fun `given disabled input entry return not included summary`() {
        val item = InputSettingsDetailsListItem("", true, false, "1.00", 1, 2)

        val result = tested.getSummary(context, item)

        verify(context).getString(R.string.price_disabled)
        assertEquals("Not included", result)
    }

    @Test
    fun `given input entry return value with measure summary`() {
        val item = InputSettingsDetailsListItem("", true, true, "1.00", 1, 2)

        val result = tested.getSummary(context, item)

        assertEquals("1.00 [kWh]", result)
    }

    @Test
    fun `given picking entry return value summary`() {
        val item = PickingSettingsDetailsListItem(3, 2, emptyList())

        val result = tested.getSummary(context, item)

        assertEquals("option1", result)
    }
}
