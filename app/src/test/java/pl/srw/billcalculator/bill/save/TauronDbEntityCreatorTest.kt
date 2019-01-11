package pl.srw.billcalculator.bill.save

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Assert.assertTrue
import org.junit.Test
import pl.srw.billcalculator.db.TauronG11Bill
import pl.srw.billcalculator.db.TauronG12Bill
import pl.srw.billcalculator.settings.prices.TauronPrices
import pl.srw.billcalculator.type.Provider
import pl.srw.billcalculator.util.ProviderMapper

class TauronDbEntityCreatorTest {

    val sharedPrices: TauronPrices = mock()
    val providerMapper: ProviderMapper = mock {
        on { getPrefsPrices(Provider.TAURON) } doReturn sharedPrices
    }

    private val sut = TauronDbEntityCreator(providerMapper)

    @Test
    fun `creates DB prices from conversion`() {
        sut.createDbPrices()

        verify(sharedPrices).convertToDb()
    }

    @Test
    fun `creates G11 DB bill when double readings not set`() {
        val input = buildInput(readingDayTo = 0, readingNightTo = 0)

        val result = sut.createDbBill(mockTauronPrices(), input)

        assertTrue(result is TauronG11Bill)
    }

    @Test
    fun `creates G12 DB bill when double readings are set`() {
        val input = buildInput(readingFrom = 0, readingTo = 0)

        val result = sut.createDbBill(mockTauronPrices(), input)

        assertTrue(result is TauronG12Bill)
    }

    private fun mockTauronPrices(): pl.srw.billcalculator.db.TauronPrices = mock {
        on { energiaElektrycznaCzynna } doReturn "0.00"
        on { oplataDystrybucyjnaZmienna } doReturn "0.00"
        on { oplataDystrybucyjnaStala } doReturn "0.00"
        on { oplataPrzejsciowa } doReturn "0.00"
        on { oplataAbonamentowa } doReturn "0.00"
        on { oplataOze } doReturn "0.00"
        on { energiaElektrycznaCzynnaDzien } doReturn "0.00"
        on { oplataDystrybucyjnaZmiennaDzien } doReturn "0.00"
        on { energiaElektrycznaCzynnaNoc } doReturn "0.00"
        on { oplataDystrybucyjnaZmiennaNoc } doReturn "0.00"
        on { oplataHandlowa } doReturn "0.00"
    }
}
