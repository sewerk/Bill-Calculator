package pl.srw.billcalculator.bill.save

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Assert.assertTrue
import org.junit.Test
import pl.srw.billcalculator.db.PgeG11Bill
import pl.srw.billcalculator.db.PgeG12Bill
import pl.srw.billcalculator.settings.prices.PgePrices
import pl.srw.billcalculator.type.Provider
import pl.srw.billcalculator.util.ProviderMapper

class PgeDbEntityCreatorTest {

    val sharedPrices: PgePrices = mock()
    val providerMapper: ProviderMapper = mock {
        on { getPrefsPrices(Provider.PGE) } doReturn sharedPrices
    }

    val sut = PgeDbEntityCreator(providerMapper)

    @Test
    fun `creates DB prices from conversion`() {
         sut.createDbPrices()

        verify(sharedPrices).convertToDb()
    }

    @Test
    fun `creates G11 DB bill when double readings not set`() {
        val input = buildInput(readingDayTo = 0, readingNightTo = 0)

        val result = sut.createDbBill(mockPgePrices(), input)

        assertTrue(result is PgeG11Bill)
    }

    @Test
    fun `creates G12 DB bill when double readins are set`() {
        val input = buildInput(readingFrom = 0, readingTo = 0)

        val result = sut.createDbBill(mockPgePrices(), input)

        assertTrue(result is PgeG12Bill)
    }

    private fun mockPgePrices(): pl.srw.billcalculator.db.PgePrices = mock {
        on { oplataAbonamentowa } doReturn "0.00"
        on { oplataPrzejsciowa } doReturn "0.00"
        on { oplataSieciowa } doReturn "0.00"
        on { oplataStalaZaPrzesyl } doReturn "0.00"
        on { skladnikJakosciowy } doReturn "0.00"
        on { zaEnergieCzynna } doReturn "0.00"
        on { oplataOze } doReturn "0.00"
        on { oplataSieciowaDzien } doReturn "0.00"
        on { oplataSieciowaNoc } doReturn "0.00"
        on { zaEnergieCzynnaDzien } doReturn "0.00"
        on { zaEnergieCzynnaNoc } doReturn "0.00"
    }
}
