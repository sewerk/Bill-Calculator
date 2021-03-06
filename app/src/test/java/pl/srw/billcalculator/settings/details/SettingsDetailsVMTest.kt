package pl.srw.billcalculator.settings.details

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.verify
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import pl.srw.billcalculator.R
import pl.srw.billcalculator.data.settings.prices.*
import pl.srw.billcalculator.type.Provider

@RunWith(JUnitParamsRunner::class)
class SettingsDetailsVMTest {

    @get:Rule val rule: TestRule = InstantTaskExecutorRule()

    val settings = spy(MutableLiveData<ProviderSettings>())
    val pricesRepo: PricesRepo = mock {
        on { pgeSettings } doReturn settings
        on { pgnigSettings } doReturn settings
        on { tauronSettings } doReturn settings
    }
    val sut = SettingsDetailsVM(pricesRepo)

    @Test
    fun `items contains input item from prices map`() {
        val provider = Provider.PGNIG
        val title = "price1"
        val price = "1.00"
        val measure = PriceMeasure.KWH
        val providerSettings = SimpleProviderSettings(provider, mapOf(title to AlwaysEnabledPriceValue(price, measure)))
        settings.value = providerSettings

        sut.listItemsFor(provider)

        assertEquals(1, sut.items.size)
        val item = sut.items[0] as InputSettingsDetailsListItem
        assertEquals(title, item.title)
        assertEquals(price, item.value)
        assertEquals(measure.resId, item.measure)
    }

    @Test
    fun `item marked non-optional and enabled for AlwaysEnabledPriceValue`() {
        val provider = Provider.PGNIG
        val title = "price1"
        val price = "1.00"
        val measure = PriceMeasure.KWH
        val providerSettings = SimpleProviderSettings(provider, mapOf(title to AlwaysEnabledPriceValue(price, measure)))
        settings.value = providerSettings

        sut.listItemsFor(provider)

        val item = sut.items[0] as InputSettingsDetailsListItem
        assertEquals(false, item.optional)
        assertEquals(true, item.enabled)
    }

    @Test
    fun `item marked optional and matches enabled flag for OptionalPriceValue`() {
        val provider = Provider.PGNIG
        val enabled = false
        val providerSettings = SimpleProviderSettings(provider, mapOf("price1" to OptionalPriceValue("1.00", PriceMeasure.KWH, enabled)))
        settings.value = providerSettings

        sut.listItemsFor(provider)

        val item = sut.items[0] as InputSettingsDetailsListItem
        assertEquals(true, item.optional)
        assertEquals(enabled, item.enabled)
    }

    @Test
    fun `items contains picking item for tariff PGE provider settings`() {
        val provider = Provider.PGE
        val tariff = EnergyTariff.G12
        val providerSettings = TariffProviderSettings(provider, tariff, mapOf())
        settings.value = providerSettings

        sut.listItemsFor(provider)

        assertEquals(1, sut.items.size)
        val item = sut.items[0] as PickingSettingsDetailsListItem
        assertEquals(R.string.settings_pge_tariff_title, item.title)
        assertEquals(tariff.stringRes, item.value)
    }

    @Test
    fun `items contains picking item for tariff TAURON provider settings`() {
        val provider = Provider.TAURON
        val tariff = EnergyTariff.G11
        val providerSettings = TariffProviderSettings(provider, tariff, mapOf())
        settings.value = providerSettings

        sut.listItemsFor(provider)

        assertEquals(1, sut.items.size)
        val item = sut.items[0] as PickingSettingsDetailsListItem
        assertEquals(R.string.settings_tauron_tariff, item.title)
        assertEquals(tariff.stringRes, item.value)
    }

    @Parameters("PGE", "PGNIG", "TAURON")
    @Test fun `updates price in repository when input value changed`(provider: Provider) {
        val title = "name"
        val value = "value"
        val enabled = true
        sut.listItemsFor(provider)

        sut.valueChanged(title, value, enabled)

        verify(pricesRepo).updatePrice(provider, title, value, enabled)
    }

    @Parameters("PGE", "PGNIG", "TAURON")
    @Test fun `if value changed to empty, then update price to default value`(provider: Provider) {
        val title = "name"
        sut.listItemsFor(provider)

        sut.valueChanged(title, "", true)

        verify(pricesRepo).updatePrice(provider, title, "0.00", true)
    }

    @Parameters("PGE", "PGNIG", "TAURON")
    @Test fun `if value changed to zero-dot, then update price to default value`(provider: Provider) {
        val title = "name"
        sut.listItemsFor(provider)

        sut.valueChanged(title, "0.", true)

        verify(pricesRepo).updatePrice(provider, title, "0.00", true)
    }

    @Parameters("PGE", "PGNIG", "TAURON")
    @Test fun `if value changed to zero, then update price to default value`(provider: Provider) {
        val title = "name"
        sut.listItemsFor(provider)

        sut.valueChanged(title, "0", true)

        verify(pricesRepo).updatePrice(provider, title, "0.00", true)
    }

    @Parameters("PGE", "PGNIG", "TAURON")
    @Test fun `if value changed to dot, then update price to default value`(provider: Provider) {
        val title = "name"
        sut.listItemsFor(provider)

        sut.valueChanged(title, ".", true)

        verify(pricesRepo).updatePrice(provider, title, "0.00", true)
    }

    @Parameters("PGE", "PGNIG", "TAURON")
    @Test fun `if value changed to dot-something, then update price to zero-dot-something`(provider: Provider) {
        val title = "name"
        sut.listItemsFor(provider)

        sut.valueChanged(title, ".9", true)

        verify(pricesRepo).updatePrice(provider, title, "0.9", true)
    }

    @Test fun `updates tariff in repository when option picked`() {
        val provider = Provider.PGE
        val tariff = EnergyTariff.G12
        given_providerSettings(provider, EnergyTariff.G11)

        sut.optionPicked(provider.tariffResource, tariff.stringRes)

        verify(pricesRepo).updateTariff(provider, tariff)
    }

    @Test
    fun `does nothing if picked current value`() {
        val provider = Provider.PGE
        val tariff = EnergyTariff.G12
        given_providerSettings(provider, tariff)

        sut.optionPicked(provider.tariffResource, tariff.stringRes)

        verify(pricesRepo, never()).updateTariff(provider, tariff)
    }

    @Parameters("PGE", "PGNIG", "TAURON")
    @Test fun `on cleanup remove observers from repository`(provider: Provider) {
        sut.listItemsFor(provider)

        sut.onCleared()

        verify(settings).removeObserver(any())
    }

    @Parameters("PGE", "PGNIG", "TAURON")
    @Test fun `on second entry remove observers from repository`(provider: Provider) {
        sut.listItemsFor(provider)

        sut.listItemsFor(Provider.PGE)

        verify(settings).removeObserver(any())
    }

    private fun given_providerSettings(provider: Provider, tariff: EnergyTariff) {
        val providerSettings = TariffProviderSettings(provider, tariff, mapOf())
        settings.value = providerSettings
        sut.listItemsFor(provider)
    }
}
