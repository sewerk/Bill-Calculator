package pl.srw.billcalculator.data.settings.prices

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.any
import pl.srw.billcalculator.settings.prices.SharedPreferencesEnergyPrices
import pl.srw.billcalculator.type.Provider
import pl.srw.billcalculator.util.ProviderMapper

class PricesBridgeTest {

    val prices: SharedPreferencesEnergyPrices = mock()
    val providerMapper: ProviderMapper = mock {
        on { getPrices(any()) } doReturn prices
    }

    val sut = PricesBridge(providerMapper)

    @Test
    fun `returns PGE tariff from shared prefs`() {
        whenever(prices.tariff).thenReturn(SharedPreferencesEnergyPrices.TARIFF_G12)

        val result = sut.getTariff(Provider.PGE)

        assertEquals(EnergyTariff.G12, result)
    }

    @Test
    fun `returns TAURON tariff from shared prefs`() {
        whenever(prices.tariff).thenReturn(SharedPreferencesEnergyPrices.TARIFF_G11)

        val result = sut.getTariff(Provider.TAURON)

        assertEquals(EnergyTariff.G11, result)
    }
}