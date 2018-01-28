package pl.srw.billcalculator.settings.details

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableArrayList
import pl.srw.billcalculator.R
import pl.srw.billcalculator.data.settings.prices.PricesRepo
import pl.srw.billcalculator.data.settings.prices.ProviderSettings
import pl.srw.billcalculator.data.settings.prices.TariffProviderSettings
import pl.srw.billcalculator.settings.details.dialog.SettingsTitleDescriptionMatcher
import pl.srw.billcalculator.type.EnumVariantNotHandledException
import pl.srw.billcalculator.type.Provider
import timber.log.Timber

class SettingsDetailsVM(private val pricesRepo: PricesRepo) : ViewModel() {

    val items = ObservableArrayList<SettingsDetailsListItem>()

    private lateinit var provider: Provider
    private val settingsChangesObserver = Observer<ProviderSettings> { transform(it!!)}

    fun listItemsFor(provider: Provider) {
        Timber.d("Getting settings details list for $provider")
        this.provider = provider
        when (provider) {
            Provider.PGE -> pricesRepo.pgeSettings.observeForever(settingsChangesObserver)
            Provider.PGNIG -> pricesRepo.pgnigSettings.observeForever(settingsChangesObserver)
            Provider.TAURON -> pricesRepo.tauronSettings.observeForever(settingsChangesObserver)
        }
    }

    fun updatePrice(title: String, value: String) {
        // TODO : validate value
        pricesRepo.updatePrice(provider, title, value)
    }

    override fun onCleared() {
        super.onCleared()
        pricesRepo.pgeSettings.removeObserver(settingsChangesObserver)
        pricesRepo.pgnigSettings.removeObserver(settingsChangesObserver)
        pricesRepo.tauronSettings.removeObserver(settingsChangesObserver)
    }

    private fun transform(data: ProviderSettings) {
        Timber.d("Setting settings details list")
        val newItems = mutableListOf<SettingsDetailsListItem>()
        if (data is TariffProviderSettings) {
            newItems += PickingSettingsDetailsListItem(getTariffResource(), data.tariff.summaryRes)
        }
        for ((title, price) in data.prices) {
            newItems += InputSettingsDetailsListItem(
                    title = title,
                    value = price.value,
                    measure = price.measure.resId,
                    description = SettingsTitleDescriptionMatcher.mapping[title])
        }
        items.clear()
        items.addAll(newItems)
    }

    private fun getTariffResource() = when (provider) {
        Provider.PGE -> R.string.settings_pge_tariff_title
        Provider.TAURON -> R.string.settings_tauron_tariff
        else -> throw EnumVariantNotHandledException(provider)
    }
}
