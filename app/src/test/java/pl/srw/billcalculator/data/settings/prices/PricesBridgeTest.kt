package pl.srw.billcalculator.data.settings.prices

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
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

    val pgePrices: PgePrices = mock(defaultAnswer = Mockito.RETURNS_MOCKS)
    val tauronPrices: TauronPrices = mock(defaultAnswer = Mockito.RETURNS_MOCKS)
    val pgnigPrices: PgnigPrices = mock(defaultAnswer = Mockito.RETURNS_MOCKS)
    val providerMapper: ProviderMapper = mock {
        on { getPrices(Provider.PGE) } doReturn pgePrices
        on { getPrices(Provider.PGNIG) } doReturn pgnigPrices
        on { getPrices(Provider.TAURON) } doReturn tauronPrices
    }

    val sut = PricesBridge(providerMapper)

    @Test fun `returns PGE tariff from shared prefs`() {
        whenever(pgePrices.tariff).thenReturn(SharedPreferencesEnergyPrices.TARIFF_G12)
        whenever(providerMapper.getPrices(Provider.PGE)).thenReturn(pgePrices)

        val result = sut.getTariff(Provider.PGE)

        assertEquals(EnergyTariff.G12, result)
    }

    @Test fun `returns TAURON tariff from shared prefs`() {
        whenever(tauronPrices.tariff).thenReturn(SharedPreferencesEnergyPrices.TARIFF_G11)
        whenever(providerMapper.getPrices(Provider.TAURON)).thenReturn(tauronPrices)

        val result = sut.getTariff(Provider.TAURON)

        assertEquals(EnergyTariff.G11, result)
    }

    @Test fun `return PGNIG prices from shared prefs`() {
        val paliwoPrice = "1.23"
        val dystStalaPrice = "3.21"
        whenever(pgnigPrices.paliwoGazowe).thenReturn(paliwoPrice)
        whenever(pgnigPrices.dystrybucyjnaStala).thenReturn(dystStalaPrice)
        whenever(providerMapper.getPrices(Provider.PGNIG)).thenReturn(pgnigPrices)

        val result = sut.getItemsForPgnig()

        assertEquals(paliwoPrice, result[PGNIG.PALIWO_GAZ]!!.value)
        assertEquals(dystStalaPrice, result[PGNIG.DYSTR_STALA]!!.value)
    }

    @Test fun `returns PGE G11 prices from shared prefs`() {
        val energiaPrice = "1.11"
        val abonamentPrice = "2.33"
        whenever(pgePrices.zaEnergieCzynna).thenReturn(energiaPrice)
        whenever(pgePrices.oplataAbonamentowa).thenReturn(abonamentPrice)
        whenever(providerMapper.getPrices(Provider.PGE)).thenReturn(pgePrices)

        val result = sut.getItemsForPgeG11()

        assertEquals(energiaPrice, result[PGE.ENERGIA]!!.value)
        assertEquals(abonamentPrice, result[PGE.ABONAMENTOWA]!!.value)
    }

    @Test fun `returns PGE G12 prices from shared prefs`() {
        val energiaDzienPrice = "1.12"
        val ozePrice = "2.23"
        whenever(pgePrices.zaEnergieCzynnaDzien).thenReturn(energiaDzienPrice)
        whenever(pgePrices.oplataOze).thenReturn(ozePrice)
        whenever(providerMapper.getPrices(Provider.PGE)).thenReturn(pgePrices)

        val result = sut.getItemsForPgeG12()

        assertEquals(energiaDzienPrice, result[PGE.ENERGIA_DZIEN]!!.value)
        assertEquals(ozePrice, result[PGE.OZE]!!.value)
    }

    @Test fun `returns TAURON G11 prices from shared prefs`() {
        val przejsciowaPrice = "4.11"
        val dystrZmiennaPrice = "2.343"
        whenever(tauronPrices.oplataPrzejsciowa).thenReturn(przejsciowaPrice)
        whenever(tauronPrices.oplataDystrybucyjnaZmienna).thenReturn(dystrZmiennaPrice)
        whenever(providerMapper.getPrices(Provider.TAURON)).thenReturn(tauronPrices)

        val result = sut.getItemsForTauronG11()

        assertEquals(przejsciowaPrice, result[TAURON.PRZEJSCIOWA]!!.value)
        assertEquals(dystrZmiennaPrice, result[TAURON.DYST_ZMIENNA]!!.value)
    }

    @Test fun `returns TAURON G12 prices from shared prefs`() {
        val energiaNocPrice = "1.171"
        val abonamentowaPrice = "7.33"
        whenever(tauronPrices.energiaElektrycznaCzynnaNoc).thenReturn(energiaNocPrice)
        whenever(tauronPrices.oplataAbonamentowa).thenReturn(abonamentowaPrice)
        whenever(providerMapper.getPrices(Provider.TAURON)).thenReturn(tauronPrices)

        val result = sut.getItemsForTauronG12()

        assertEquals(energiaNocPrice, result[TAURON.ENERGIA_NOC]!!.value)
        assertEquals(abonamentowaPrice, result[TAURON.ABONAMENTOWA]!!.value)
    }

    @Test fun `update PGNIG by shared prefs`() {
        val value = "1.2"

        sut.updatePgnig(PGNIG.WSP_KONW, value)

        verify(pgnigPrices).wspolczynnikKonwersji = value
    }

    @Test fun `update PGE by shared prefs`() {
        val value = "1.2"

        sut.updatePge(PGE.SIECIOWA, value)

        verify(pgePrices).oplataSieciowa = value
    }

    @Test fun `update TAURON by shared prefs`() {
        val value = "1.2"

        sut.updateTauron(TAURON.DYST_STALA, value)

        verify(tauronPrices).oplataDystrybucyjnaStala = value
    }

    @Test fun `set defaults for PGE to shared prefs`() {
        sut.setDefaults(Provider.PGE)

        verify(pgePrices).setDefault()
    }

    @Test fun `set defaults for PGNIG to shared prefs`() {
        sut.setDefaults(Provider.PGNIG)

        verify(pgnigPrices).setDefault()
    }

    @Test fun `set defaults for TAURON to shared prefs`() {
        sut.setDefaults(Provider.TAURON)

        verify(tauronPrices).setDefault()
    }
}
