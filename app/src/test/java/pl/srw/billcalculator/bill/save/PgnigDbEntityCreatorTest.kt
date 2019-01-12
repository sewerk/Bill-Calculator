package pl.srw.billcalculator.bill.save

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Assert.assertTrue
import org.junit.Test
import pl.srw.billcalculator.db.PgnigBill
import pl.srw.billcalculator.settings.prices.PgnigPrices
import pl.srw.billcalculator.type.Provider
import pl.srw.billcalculator.util.ProviderMapper

class PgnigDbEntityCreatorTest {

    val sharedPrices: PgnigPrices = mock()
    val providerMapper: ProviderMapper = mock {
        on { getPrefsPrices(Provider.PGNIG) } doReturn sharedPrices
    }

    private val sut = PgnigDbEntityCreator(providerMapper)

    @Test
    fun `creates DB prices from conversion`() {
        sut.createDbPrices()

        verify(sharedPrices).convertToDb()
    }

    @Test
    fun `creates DB bill`() {
        val result = sut.createDbBill(mockPgnigPrices(), buildInput())

        assertTrue(result is PgnigBill)
    }

    private fun mockPgnigPrices(): pl.srw.billcalculator.db.PgnigPrices = mock {
        on { dystrybucyjnaStala } doReturn "0.00"
        on { dystrybucyjnaZmienna } doReturn "0.00"
        on { oplataAbonamentowa } doReturn "0.00"
        on { paliwoGazowe } doReturn "0.00"
        on { wspolczynnikKonwersji } doReturn "0.00"
        on { oplataHandlowa } doReturn "0.00"
    }
}
