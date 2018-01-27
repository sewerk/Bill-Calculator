package pl.srw.billcalculator.settings.details

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableArrayList
import pl.srw.billcalculator.R
import pl.srw.billcalculator.data.settings.prices.PricesRepo
import pl.srw.billcalculator.data.settings.prices.TariffProviderSettings
import pl.srw.billcalculator.settings.details.dialog.SettingsTitleDescriptionMatcher
import pl.srw.billcalculator.type.EnumVariantNotHandledException
import pl.srw.billcalculator.type.Provider
import timber.log.Timber

class SettingsDetailsVM(private val pricesRepo: PricesRepo) : ViewModel() {

    val items = ObservableArrayList<SettingsDetailsListItem>()

    fun updateItemsFor(provider: Provider) {
        Timber.d("getting settings details list for $provider")
        val newItems = mutableListOf<SettingsDetailsListItem>()
        val data = pricesRepo.getProviderSettings(provider)
        if (data is TariffProviderSettings) {
            newItems += PickingSettingsDetailsListItem(getTariffResource(provider), data.tariff.summaryRes)
        }
        for ((title, price) in data.prices) {
            newItems += InputSettingsDetailsListItem(
                    title = title,
                    summary = price.value,
                    measure = price.measure.resId,
                    description = SettingsTitleDescriptionMatcher.mapping[title])
        }
        items.clear()
        items.addAll(newItems)
    }

    private fun getTariffResource(provider: Provider) = when (provider) {
        Provider.PGE -> R.string.settings_pge_tariff_title
        Provider.TAURON -> R.string.settings_tauron_tariff
        else -> throw EnumVariantNotHandledException(provider)
    }
}
