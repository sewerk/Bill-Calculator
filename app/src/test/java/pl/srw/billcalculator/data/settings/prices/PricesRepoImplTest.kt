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

    @Test fun `returns new PGE tariff value if changed through bridge`() {
        val provider = Provider.PGE
        val tariff = EnergyTariff.G12
        whenever(pricesBridge.getTariff(provider)).thenReturn(tariff)

        sut.updateTariff(provider, tariff)

        assertEquals(tariff, sut.tariffPge.value)
    }

    @Test fun `returns new TAURON tariff value if changed through bridge`() {
        val provider = Provider.TAURON
        val tariff = EnergyTariff.G12
        whenever(pricesBridge.getTariff(provider)).thenReturn(tariff)

        sut.updateTariff(provider, tariff)

        assertEquals(tariff, sut.tariffTauron.value)
    }

    @Parameters("PGE", "TAURON")
    @Test fun `updates tariff through bridge`(provider: Provider) {
        val tariff = EnergyTariff.G12

        sut.updateTariff(provider, tariff)

        verify(pricesBridge).updateTariff(provider, tariff)
    }

    @Test(expected = EnumVariantNotHandledException::class)
    fun `throws exception if tariff change for non-energy provider`() {
        sut.updateTariff(Provider.PGNIG, EnergyTariff.G11)
    }

    @Test fun `refresh provider settings for PGE, when tariff changed`() {
        val observer: Observer<ProviderSettings?> = mock()
        sut.pgeSettings.observeForever(observer)
        clearInvocations(observer)

        sut.updateTariff(Provider.PGE, EnergyTariff.G12)

        verify(observer).onChanged(any())
    }

    @Test fun `refresh provider settings for TAURON, when tariff changed`() {
        val observer: Observer<ProviderSettings?> = mock()
        sut.tauronSettings.observeForever(observer)
        clearInvocations(observer)

        sut.updateTariff(Provider.TAURON, EnergyTariff.G12)

        verify(observer).onChanged(any())
    }

    @Test fun `prices are taken from prices bridge`() {
        val prices = mapOf<String, PriceValue>()
        whenever(pricesBridge.getItemsForPgnig()).thenReturn(prices)

        assertEquals(prices, sut.pgnigSettings.value!!.prices)
    }

    @Test fun `update PGE prices through bridge`() {
        val priceValue = "1"
        val priceName = "energia"
        val enabled = true

        sut.updatePrice(Provider.PGE, priceName, priceValue, enabled)

        verify(pricesBridge).updatePge(priceName, priceValue, enabled)
    }

    @Test fun `update PGNIG prices through bridge`() {
        val priceValue = "1"
        val priceName = "energia"
        val enabled = true

        sut.updatePrice(Provider.PGNIG, priceName, priceValue, enabled)

        verify(pricesBridge).updatePgnig(priceName, priceValue, enabled)
    }

    @Test fun `update TAURON prices through bridge`() {
        val priceValue = "1"
        val priceName = "energia"
        val enabled = true

        sut.updatePrice(Provider.TAURON, priceName, priceValue, enabled)

        verify(pricesBridge).updateTauron(priceName, priceValue, enabled)
    }

    @Test fun `refreshes current settings values, when update prices for PGE`() {
        val observer: Observer<ProviderSettings?> = mock()
        sut.pgeSettings.observeForever(observer)
        clearInvocations(observer)

        sut.updatePrice(Provider.PGE, "name", "value", true)

        verify(observer).onChanged(any())
    }

    @Test fun `refreshes current settings values, when update prices for PGNIG`() {
        val observer: Observer<ProviderSettings?> = mock()
        sut.pgnigSettings.observeForever(observer)
        clearInvocations(observer)

        sut.updatePrice(Provider.PGNIG, "name", "value", true)

        verify(observer).onChanged(any())
    }

    @Test fun `refreshes current settings values, when update prices for TAURON`() {
        val observer: Observer<ProviderSettings?> = mock()
        sut.tauronSettings.observeForever(observer)
        clearInvocations(observer)

        sut.updatePrice(Provider.TAURON, "name", "value", true)

        verify(observer).onChanged(any())
    }

    @Parameters("PGE", "PGNIG", "TAURON")
    @Test fun `set defaults updated through bridge`(provider: Provider) {
        sut.setDefaultPricesFor(provider)

        verify(pricesBridge).setDefaults(provider)
    }

    @Test fun `refreshes PGE tariff to G11, when set defaults for PGE`() {
        val observer: Observer<EnergyTariff?> = mock()
        sut.tariffPge.observeForever(observer)
        clearInvocations(observer)

        sut.setDefaultPricesFor(Provider.PGE)

        verify(observer).onChanged(EnergyTariff.G11)
    }

    @Test fun `refreshes TAURON tariff to G11, when set defaults for TAURON`() {
        val observer: Observer<EnergyTariff?> = mock()
        sut.tariffTauron.observeForever(observer)
        clearInvocations(observer)

        sut.setDefaultPricesFor(Provider.TAURON)

        verify(observer).onChanged(EnergyTariff.G11)
    }

    @Test fun `refreshes PGE current settings, when set defaults for PGE`() {
        val observer: Observer<ProviderSettings?> = mock()
        sut.pgeSettings.observeForever(observer)
        clearInvocations(observer)

        sut.setDefaultPricesFor(Provider.PGE)

        verify(observer).onChanged(any())
    }

    @Test fun `refreshes PGNIG current settings, when set defaults for PGNIG`() {
        val observer: Observer<ProviderSettings?> = mock()
        sut.pgnigSettings.observeForever(observer)
        clearInvocations(observer)

        sut.setDefaultPricesFor(Provider.PGNIG)

        verify(observer).onChanged(any())
    }

    @Test fun `refreshes TAURON current settings, when set defaults for TAURON`() {
        val observer: Observer<ProviderSettings?> = mock()
        sut.tauronSettings.observeForever(observer)
        clearInvocations(observer)

        sut.setDefaultPricesFor(Provider.TAURON)

        verify(observer).onChanged(any())
    }
}
