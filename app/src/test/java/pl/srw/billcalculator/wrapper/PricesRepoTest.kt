package pl.srw.billcalculator.wrapper

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import pl.srw.billcalculator.settings.prices.SharedPreferencesEnergyPrices
import pl.srw.billcalculator.type.Provider
import pl.srw.billcalculator.util.ProviderMapper

class PricesRepoTest {

    @get:Rule val rule: TestRule = InstantTaskExecutorRule()

    val defaultTariff = SharedPreferencesEnergyPrices.TARIFF_G11
    val prices: SharedPreferencesEnergyPrices = mock {
        on { tariff } doReturn defaultTariff
    }
    val providerMapper: ProviderMapper = mock {
        on { getPrices(any()) } doReturn prices
    }

    val sut by lazy { PricesRepo(providerMapper) } // to delay init after rule is ready

    @Test
    fun `return default tariff if not changed`() {
        assert(defaultTariff == sut.tariffPge.value)
        assert(defaultTariff == sut.tariffTauron.value)
    }

    @Test
    fun `returns new tariff if changed for PGE`() {
        val tariff = SharedPreferencesEnergyPrices.TARIFF_G12

        sut.updateTariff(Provider.PGE, tariff)
        assert(tariff == sut.tariffPge.value)
    }

    @Test
    fun `returns new tariff if changed for TAURON`() {
        val tariff = SharedPreferencesEnergyPrices.TARIFF_G12

        sut.updateTariff(Provider.TAURON, tariff)
        assert(tariff == sut.tariffTauron.value)
    }

    @Test
    fun `prices are taken from providerMapper`() {
        whenever(providerMapper.getPrices(Provider.PGE)).thenReturn(prices)

        assert(prices == sut.getPrices(Provider.PGE))
    }

    @Test
    fun `tariff is taken from prices`() {
        val tariff = SharedPreferencesEnergyPrices.TARIFF_G12
        whenever(prices.tariff).thenReturn(tariff)

        assert(tariff == sut.getTariff(Provider.PGE))
    }
}
