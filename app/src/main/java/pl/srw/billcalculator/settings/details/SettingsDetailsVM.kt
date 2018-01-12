package pl.srw.billcalculator.settings.details

import android.arch.lifecycle.ViewModel
import pl.srw.billcalculator.R
import pl.srw.billcalculator.data.settings.prices.PricesRepo
import pl.srw.billcalculator.data.settings.prices.TariffProviderSettings
import pl.srw.billcalculator.type.EnumVariantNotHandledException
import pl.srw.billcalculator.type.Provider

class SettingsDetailsVM(private val pricesRepo: PricesRepo) : ViewModel() {

    lateinit var items: List<SettingsDetailsListItem>
        private set

    fun getFor(provider: Provider) {
        val newItems = mutableListOf<SettingsDetailsListItem>()
        val data = pricesRepo.getProviderSettings(provider)
        if (data is TariffProviderSettings) {
            newItems += PickingSettingsDetailsListItem(getTariffResource(provider), data.tariff.summaryRes)
        }
        for (entry in data.prices) {
            newItems += InputSettingsDetailsListItem(entry.key, entry.value.value, entry.value.measure.resId)
        }
        items = newItems
    }

    private fun getTariffResource(provider: Provider) = when (provider) {
        Provider.PGE -> R.string.settings_pge_tariff_title
        Provider.TAURON -> R.string.settings_tauron_tariff
        else -> throw EnumVariantNotHandledException(provider)
    }
}
