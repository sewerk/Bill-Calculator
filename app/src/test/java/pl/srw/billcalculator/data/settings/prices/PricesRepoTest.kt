package pl.srw.billcalculator.data.settings.prices

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import pl.srw.billcalculator.settings.prices.SharedPreferencesEnergyPrices
import pl.srw.billcalculator.type.Provider

class PricesRepoTest {

    @get:Rule val rule: TestRule = InstantTaskExecutorRule()

    val defaultTariff = SharedPreferencesEnergyPrices.TARIFF_G11
    val pricesBridge: PricesBridge = mock {
        on { getPgeTariff() } doReturn defaultTariff
        on { getTauronTariff() } doReturn defaultTariff
    }

    val sut by lazy { PricesRepo(pricesBridge) } // to delay init after rule is ready

    @Test
    fun `return default tariff if not changed`() {
        assertEquals(defaultTariff, sut.tariffPge.value)
        assertEquals(defaultTariff, sut.tariffTauron.value)
    }

    @Test
    fun `returns new tariff if changed for PGE`() {
        val tariff = SharedPreferencesEnergyPrices.TARIFF_G12

        sut.updateTariff(Provider.PGE, tariff)
        assertEquals(tariff, sut.tariffPge.value)
    }

    @Test
    fun `returns new tariff if changed for TAURON`() {
        val tariff = SharedPreferencesEnergyPrices.TARIFF_G12

        sut.updateTariff(Provider.TAURON, tariff)
        assertEquals(tariff, sut.tariffTauron.value)
    }

    @Test
    fun `prices are taken from prices bridge`() {
        val prices = mapOf<String, PriceValue>()
        whenever(pricesBridge.getItemsForPgnig()).thenReturn(prices)

        assertEquals(prices, sut.getProviderSettings(Provider.PGNIG).prices)
    }
}
