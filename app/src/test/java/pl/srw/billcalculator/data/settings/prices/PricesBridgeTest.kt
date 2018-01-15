package pl.srw.billcalculator.data.settings.prices

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito
import pl.srw.billcalculator.settings.prices.PgePrices
import pl.srw.billcalculator.settings.prices.PgnigPrices
import pl.srw.billcalculator.settings.prices.SharedPreferencesEnergyPrices
import pl.srw.billcalculator.settings.prices.TauronPrices
import pl.srw.billcalculator.type.Provider
import pl.srw.billcalculator.util.ProviderMapper

class PricesBridgeTest {

    val providerMapper: ProviderMapper = mock()

    val sut = PricesBridge(providerMapper)

    @Test
    fun `returns PGE tariff from shared prefs`() {
        val prices: PgePrices = mock()
        whenever(prices.tariff).thenReturn(SharedPreferencesEnergyPrices.TARIFF_G12)
        whenever(providerMapper.getPrices(Provider.PGE)).thenReturn(prices)

        val result = sut.getTariff(Provider.PGE)

        assertEquals(EnergyTariff.G12, result)
    }

    @Test
    fun `returns TAURON tariff from shared prefs`() {
        val prices: TauronPrices = mock()
        whenever(prices.tariff).thenReturn(SharedPreferencesEnergyPrices.TARIFF_G11)
        whenever(providerMapper.getPrices(Provider.TAURON)).thenReturn(prices)

        val result = sut.getTariff(Provider.TAURON)

        assertEquals(EnergyTariff.G11, result)
    }

    @Test
    fun `return PGNIG prices from shared prefs`() {
        val paliwoPrice = "1.23"
        val dystStalaPrice = "3.21"
        val prices: PgnigPrices = mock(defaultAnswer = Mockito.RETURNS_MOCKS)
        whenever(prices.paliwoGazowe).thenReturn(paliwoPrice)
        whenever(prices.dystrybucyjnaStala).thenReturn(dystStalaPrice)
        whenever(providerMapper.getPrices(Provider.PGNIG)).thenReturn(prices)

        val result = sut.getItemsForPgnig()

        assertEquals(paliwoPrice, result[PGNIG.PALIWO_GAZ]!!.value)
        assertEquals(dystStalaPrice, result[PGNIG.DYSTR_STALA]!!.value)
    }

    @Test
    fun `returns PGE G11 prices from shared prefs`() {
        val energiaPrice = "1.11"
        val abonamentPrice = "2.33"
        val prices: PgePrices = mock(defaultAnswer = Mockito.RETURNS_MOCKS)
        whenever(prices.zaEnergieCzynna).thenReturn(energiaPrice)
        whenever(prices.oplataAbonamentowa).thenReturn(abonamentPrice)
        whenever(providerMapper.getPrices(Provider.PGE)).thenReturn(prices)

        val result = sut.getItemsForPgeG11()

        assertEquals(energiaPrice, result[PGE.ENERGIA]!!.value)
        assertEquals(abonamentPrice, result[PGE.ABONAMENTOWA]!!.value)
    }

    @Test
    fun `returns PGE G12 prices from shared prefs`() {
        val energiaDzienPrice = "1.12"
        val ozePrice = "2.23"
        val prices: PgePrices = mock(defaultAnswer = Mockito.RETURNS_MOCKS)
        whenever(prices.zaEnergieCzynnaDzien).thenReturn(energiaDzienPrice)
        whenever(prices.oplataOze).thenReturn(ozePrice)
        whenever(providerMapper.getPrices(Provider.PGE)).thenReturn(prices)

        val result = sut.getItemsForPgeG12()

        assertEquals(energiaDzienPrice, result[PGE.ENERGIA_DZIEN]!!.value)
        assertEquals(ozePrice, result[PGE.OZE]!!.value)
    }

    @Test
    fun `returns TAURON G11 prices from shared prefs`() {
        val przejsciowaPrice = "4.11"
        val dystrZmiennaPrice = "2.343"
        val prices: TauronPrices = mock(defaultAnswer = Mockito.RETURNS_MOCKS)
        whenever(prices.oplataPrzejsciowa).thenReturn(przejsciowaPrice)
        whenever(prices.oplataDystrybucyjnaZmienna).thenReturn(dystrZmiennaPrice)
        whenever(providerMapper.getPrices(Provider.TAURON)).thenReturn(prices)

        val result = sut.getItemsForTauronG11()

        assertEquals(przejsciowaPrice, result[TAURON.PRZEJSCIOWA]!!.value)
        assertEquals(dystrZmiennaPrice, result[TAURON.DYST_ZMIENNA]!!.value)
    }

    @Test
    fun `returns TAURON G12 prices from shared prefs`() {
        val energiaNocPrice = "1.171"
        val abonamentowaPrice = "7.33"
        val prices: TauronPrices = mock(defaultAnswer = Mockito.RETURNS_MOCKS)
        whenever(prices.energiaElektrycznaCzynnaNoc).thenReturn(energiaNocPrice)
        whenever(prices.oplataAbonamentowa).thenReturn(abonamentowaPrice)
        whenever(providerMapper.getPrices(Provider.TAURON)).thenReturn(prices)

        val result = sut.getItemsForTauronG12()

        assertEquals(energiaNocPrice, result[TAURON.ENERGIA_NOC]!!.value)
        assertEquals(abonamentowaPrice, result[TAURON.ABONAMENTOWA]!!.value)
    }
}
