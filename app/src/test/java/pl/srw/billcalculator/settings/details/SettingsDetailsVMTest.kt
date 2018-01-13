package pl.srw.billcalculator.settings.details

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert.assertEquals
import org.junit.Test
import pl.srw.billcalculator.R
import pl.srw.billcalculator.data.settings.prices.*
import pl.srw.billcalculator.type.Provider

class SettingsDetailsVMTest {

    val pricesRepo: PricesRepo = mock()
    val sut = SettingsDetailsVM(pricesRepo)

    @Test
    fun `items contains input item from prices map`() {
        val provider = Provider.PGNIG
        val title = "price1"
        val price = "1.00"
        val measure = PriceMeasure.KWH
        val providerSettings = SimpleProviderSettings(provider, mapOf(title to PriceValue(price, measure)))
        whenever(pricesRepo.getProviderSettings(provider)).thenReturn(providerSettings)

        sut.getFor(provider)

        assertEquals(1, sut.items.size)
        val item = sut.items[0] as InputSettingsDetailsListItem
        assertEquals(title, item.title)
        assertEquals(price, item.summary)
        assertEquals(measure.resId, item.measure)
    }

    @Test
    fun `items contains picking item for tariff PGE provider settings`() {
        val provider = Provider.PGE
        val tariff = EnergyTariff.G12
        val providerSettings = TariffProviderSettings(provider, tariff, mapOf())
        whenever(pricesRepo.getProviderSettings(provider)).thenReturn(providerSettings)

        sut.getFor(provider)

        assertEquals(1, sut.items.size)
        val item = sut.items[0] as PickingSettingsDetailsListItem
        assertEquals(R.string.settings_pge_tariff_title, item.title)
        assertEquals(tariff.summaryRes, item.summary)
    }

    @Test
    fun `items contains picking item for tariff TAURON provider settings`() {
        val provider = Provider.TAURON
        val tariff = EnergyTariff.G11
        val providerSettings = TariffProviderSettings(provider, tariff, mapOf())
        whenever(pricesRepo.getProviderSettings(provider)).thenReturn(providerSettings)

        sut.getFor(provider)

        assertEquals(1, sut.items.size)
        val item = sut.items[0] as PickingSettingsDetailsListItem
        assertEquals(R.string.settings_tauron_tariff, item.title)
        assertEquals(tariff.summaryRes, item.summary)
    }
}