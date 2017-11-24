@file:Suppress("IllegalIdentifier")
package pl.srw.billcalculator.form

import org.junit.Assert.assertEquals
import org.junit.Test
import org.threeten.bp.LocalDate

class FormVMTest {

    val sut = FormVM()

    @Test
    fun `initialize fromDate with first day of current month`() {
        assertEquals(calculateDateFrom(), sut.fromDate)
    }

    @Test
    fun `initialize toDate with last dat of current month`() {
        assertEquals(calculateDateTo(), sut.toDate)
    }

    private fun calculateDateFrom(): LocalDate = LocalDate.now().withDayOfMonth(1)

    private fun calculateDateTo(): LocalDate {
        val now = LocalDate.now()
        return now.withDayOfMonth(now.lengthOfMonth())
    }
}