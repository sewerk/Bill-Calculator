package pl.srw.billcalculator.data.settings.prices

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.clearInvocations
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import pl.srw.billcalculator.type.EnumVariantNotHandledException
import pl.srw.billcalculator.type.Provider

@RunWith(JUnitParamsRunner::class)
class PricesRepoImplTest {

    @get:Rule val rule: TestRule = InstantTaskExecutorRule()

    val defaultTariff = EnergyTariff.G11
    val pricesBridge: PricesBridge = mock {
        on { getTariff(any()) } doReturn defaultTariff
    }

    val sut by lazy { PricesRepoImpl(pricesBridge) } // to delay init after rule is ready

    @Test fun `return default tariff if not changed`() {
        assertEquals(defaultTariff, sut.tariffPge.value)
        assertEquals(defaultTariff, sut.tariffTauron.value)
    }

    @Test fun `returns new tariff if changed for PGE`() {
        val tariff = EnergyTariff.G12

        sut.updateTariff(Provider.PGE, tariff)

        assertEquals(tariff, sut.tariffPge.value)
    }

    @Test fun `returns new tariff if changed for TAURON`() {
        val tariff = EnergyTariff.G12

        sut.updateTariff(Provider.TAURON, tariff)

        assertEquals(tariff, sut.tariffTauron.value)
    }

    @Test(expected = EnumVariantNotHandledException::class)
    fun `throws exception if tariff change for non-energy provider`() {
        sut.updateTariff(Provider.PGNIG, EnergyTariff.G11)
    }

    @Test fun `prices are taken from prices bridge`() {
        val prices = mapOf<String, PriceValue>()
        whenever(pricesBridge.getItemsForPgnig()).thenReturn(prices)

        assertEquals(prices, sut.pgnigSettings.value!!.prices)
    }

    @Test fun `update PGE prices through bridge`() {
        val priceValue = "1"
        val priceName = "energia"

        sut.updatePrice(Provider.PGE, priceName, priceValue)

        verify(pricesBridge).updatePge(priceName, priceValue)
    }

    @Test fun `update PGNIG prices through bridge`() {
        val priceValue = "1"
        val priceName = "energia"

        sut.updatePrice(Provider.PGNIG, priceName, priceValue)

        verify(pricesBridge).updatePgnig(priceName, priceValue)
    }

    @Test fun `update TAURON prices through bridge`() {
        val priceValue = "1"
        val priceName = "energia"

        sut.updatePrice(Provider.TAURON, priceName, priceValue)

        verify(pricesBridge).updateTauron(priceName, priceValue)
    }

    @Test fun `update prices for PGE refreshes current settings values`() {
        val observer: Observer<ProviderSettings?> = mock()
        sut.pgeSettings.observeForever(observer)
        clearInvocations(observer)

        sut.updatePrice(Provider.PGE, "name", "value")

        verify(observer).onChanged(any())
    }

    @Test fun `update prices for PGNIG refreshes current settings values`() {
        val observer: Observer<ProviderSettings?> = mock()
        sut.pgnigSettings.observeForever(observer)
        clearInvocations(observer)

        sut.updatePrice(Provider.PGNIG, "name", "value")

        verify(observer).onChanged(any())
    }

    @Test fun `update prices for TAURON refreshes current settings values`() {
        val observer: Observer<ProviderSettings?> = mock()
        sut.tauronSettings.observeForever(observer)
        clearInvocations(observer)

        sut.updatePrice(Provider.TAURON, "name", "value")

        verify(observer).onChanged(any())
    }

    @Parameters("PGE", "PGNIG", "TAURON")
    @Test fun `set defaults updated through bridge`(provider: Provider) {
        sut.setDefaultPricesFor(provider)

        verify(pricesBridge).setDefaults(provider)
    }

    @Test fun `set defaults for PGE refreshes current settings values`() {
        val observer: Observer<ProviderSettings?> = mock()
        sut.pgeSettings.observeForever(observer)
        clearInvocations(observer)

        sut.setDefaultPricesFor(Provider.PGE)

        verify(observer).onChanged(any())
    }

    @Test fun `set defaults for PGNIG refreshes current settings values`() {
        val observer: Observer<ProviderSettings?> = mock()
        sut.pgnigSettings.observeForever(observer)
        clearInvocations(observer)

        sut.setDefaultPricesFor(Provider.PGNIG)

        verify(observer).onChanged(any())
    }

    @Test fun `set defaults for TAURON refreshes current settings values`() {
        val observer: Observer<ProviderSettings?> = mock()
        sut.tauronSettings.observeForever(observer)
        clearInvocations(observer)

        sut.setDefaultPricesFor(Provider.TAURON)

        verify(observer).onChanged(any())
    }
}
