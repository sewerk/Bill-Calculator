package pl.srw.billcalculator.form.autocomplete

import android.content.Context
import com.nhaarman.mockito_kotlin.mock
import org.junit.Assert.*
import org.junit.Test
import pl.srw.billcalculator.getState

class PreviousReadingsAdapterTest {

    @Test
    fun `have empty String array data when constructing with empty data`() {
        val context: Context = mock()

        val sut: PreviousReadingsAdapter = PreviousReadingsAdapter(context, IntArray(0))

        val allData: Array<String> = sut.getState("allData")
        assertTrue(allData.isEmpty())
    }

    @Test
    fun `have non-empty String array data when constructing with non-empty data`() {
        val sut = PreviousReadingsAdapter(null, intArrayOf(1, 2, 3))

        val allData: Array<String> = sut.getState("allData")
        assertArrayEquals(arrayOf("1", "2", "3"), allData)
    }

    @Test
    fun `filtering returns last item when prefix is empty`() {
        val result = PreviousReadingsAdapter.filterData("", arrayOf("3", "2", "1"))

        assertEquals("3", result[0])
    }

    @Test
    fun `filtering returns empty array when data is empty`() {
        val result = PreviousReadingsAdapter.filterData("", emptyArray())

        assertTrue(result.isEmpty())
    }

    @Test
    fun `filtering returns all items matching prefix`() {
        val input = arrayOf("111", "122", "223", "344", "1", "11", "12")

        val result = PreviousReadingsAdapter.filterData("12", input)

        assertArrayEquals(arrayOf("122", "12"), result)
    }
}
