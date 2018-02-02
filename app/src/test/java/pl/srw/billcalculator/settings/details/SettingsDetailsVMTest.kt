package pl.srw.billcalculator.settings.details

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
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
import pl.srw.billcalculator.data.settings.prices.EnergyTariff
import pl.srw.billcalculator.data.settings.prices.PriceMeasure
import pl.srw.billcalculator.data.settings.prices.PriceValue
import pl.srw.billcalculator.data.settings.prices.PricesRepo
import pl.srw.billcalculator.data.settings.prices.ProviderSettings
import pl.srw.billcalculator.data.settings.prices.SimpleProviderSettings
import pl.srw.billcalculator.data.settings.prices.TariffProviderSettings
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

    @Test
    fun `items contains input item from prices map`() {
        val provider = Provider.PGNIG
        val title = "price1"
        val price = "1.00"
        val measure = PriceMeasure.KWH
        val providerSettings = SimpleProviderSettings(provider, mapOf(title to PriceValue(price, measure)))
        settings.value = providerSettings

        val sut = SettingsDetailsVM(provider, pricesRepo)

        assertEquals(1, sut.items.size)
        val item = sut.items[0] as InputSettingsDetailsListItem
        assertEquals(title, item.title)
        assertEquals(price, item.value)
        assertEquals(measure.resId, item.measure)
    }

    @Test
    fun `items contains picking item for tariff PGE provider settings`() {
        val provider = Provider.PGE
        val tariff = EnergyTariff.G12
        val providerSettings = TariffProviderSettings(provider, tariff, mapOf())
        settings.value = providerSettings

        val sut = SettingsDetailsVM(provider, pricesRepo)

        assertEquals(1, sut.items.size)
        val item = sut.items[0] as PickingSettingsDetailsListItem
        assertEquals(R.string.settings_pge_tariff_title, item.title)
        assertEquals(tariff.summaryRes, item.value)
    }

    @Test
    fun `items contains picking item for tariff TAURON provider settings`() {
        val provider = Provider.TAURON
        val tariff = EnergyTariff.G11
        val providerSettings = TariffProviderSettings(provider, tariff, mapOf())
        settings.value = providerSettings

        val sut = SettingsDetailsVM(provider, pricesRepo)

        assertEquals(1, sut.items.size)
        val item = sut.items[0] as PickingSettingsDetailsListItem
        assertEquals(R.string.settings_tauron_tariff, item.title)
        assertEquals(tariff.summaryRes, item.value)
    }

    @Parameters("PGE", "PGNIG", "TAURON")
    @Test fun `update price forward for repository`(provider: Provider) {
        val sut = SettingsDetailsVM(provider, pricesRepo)
        val title = "name"
        val value = "value"

        sut.updatePrice(title, value)

        verify(pricesRepo).updatePrice(provider, title, value)
    }

    @Parameters("PGE", "PGNIG", "TAURON")
    @Test fun `if value changed to empty then update price to default value`(provider: Provider) {
        val sut = SettingsDetailsVM(provider, pricesRepo)
        val title = "name"

        sut.updatePrice(title, "")

        verify(pricesRepo).updatePrice(provider, title, "0.00")
    }

    @Parameters("PGE", "PGNIG", "TAURON")
    @Test fun `if value changed to 0 then update price to default value`(provider: Provider) {
        val sut = SettingsDetailsVM(provider, pricesRepo)
        val title = "name"

        sut.updatePrice(title, "0.")

        verify(pricesRepo).updatePrice(provider, title, "0.00")
    }

    @Parameters("PGE", "PGNIG", "TAURON")
    @Test fun `on cleanup remove observers from repository`(provider: Provider) {
        val sut = SettingsDetailsVM(provider, pricesRepo)

        sut.onCleared()

        verify(settings).removeObserver(any())
    }
}
