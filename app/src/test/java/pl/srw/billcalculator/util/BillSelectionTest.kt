package pl.srw.billcalculator.util

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Test
import pl.srw.billcalculator.db.PgnigBill
import java.util.*

class BillSelectionTest {

    val sut = BillSelection()

    @Test
    fun `confirms selected, after selection`() {
        sut.select(1, PgnigBill())

        assertThat(sut.isSelected(1), `is`(true))
    }

    @Test
    fun `deny selection, when not selected`() {
        assertFalse(sut.isSelected(1))
    }

    @Test
    fun `deny selection, after deselection`() {
        sut.select(1, PgnigBill())

        sut.deselect(1)

        assertThat(sut.isSelected(1), `is`(false))
    }

    @Test
    fun `confirm any selection, after selection`() {
        sut.select(1, PgnigBill())

        assertThat(sut.isAnySelected(), `is`(true))
    }

    @Test
    fun `deny any selection, when none selected`() {
        assertFalse(sut.isAnySelected())
    }

    @Test
    fun `deny any selection, after deselection of only selected`() {
        sut.select(1, PgnigBill())

        sut.deselect(1)

        assertThat(sut.isAnySelected(), `is`(false))
    }

    @Test
    fun `deny any selection, after deselect all`() {
        sut.select(1, PgnigBill())

        sut.deselectAll()

        assertThat(sut.isAnySelected(), `is`(false))
    }

    @Test
    fun `items contains all selected items`() {
        val a = PgnigBill()
        val b = PgnigBill()
        val c = PgnigBill()
        sut.select(1, a)
        sut.select(3, c)
        sut.select(2, b)

        val result = sut.getItems()
        assertThat(result.size, `is`(3))
        assertThat(result.contains(a), `is`(true))
        assertThat(result.contains(b), `is`(true))
        assertThat(result.contains(c), `is`(true))
    }

    @Test
    fun `returns positions in reverse order after random selection`() {
        sut.select(3, PgnigBill())
        sut.select(0, PgnigBill())
        sut.select(4, PgnigBill())
        sut.select(2, PgnigBill())

        val result = sut.getPositionsReverseOrder()
        assertThat(result.size, `is`(4))
        assertThat(Arrays.toString(result), `is`("[4, 3, 2, 0]"))
    }

    @Test
    fun `updates positions, when element inserted with lower index`() {
        sut.select(1, PgnigBill())

        sut.onInsert(0)

        assertTrue(sut.isSelected(2))
    }

    @Test
    fun `updates positions, when element inserted with equal index`() {
        sut.select(1, PgnigBill())

        sut.onInsert(1)

        assertTrue(sut.isSelected(2))
    }

    @Test
    fun `does nothing, when element inserted with higher index`() {
        sut.select(1, PgnigBill())

        sut.onInsert(2)

        assertTrue(sut.isSelected(1))
        assertThat(sut.getPositionsReverseOrder().size, `is`(1))
    }

    @Test
    fun `updates only some positions, when multiple selections`() {
        sut.select(1, PgnigBill())
        sut.select(2, PgnigBill())
        sut.select(3, PgnigBill())

        sut.onInsert(2)

        assertTrue(sut.isSelected(1))
        assertFalse(sut.isSelected(2))
        assertTrue(sut.isSelected(3))
        assertTrue(sut.isSelected(4))
    }

    @Test
    fun `does nothing, when empty selection`() {
        sut.onInsert(1)

        assertFalse(sut.isAnySelected())
    }
}
