package pl.srw.billcalculator.settings.details

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableArrayList
import android.support.annotation.VisibleForTesting
import pl.srw.billcalculator.data.settings.prices.EnergyTariff
import pl.srw.billcalculator.data.settings.prices.OptionalPriceValue
import pl.srw.billcalculator.data.settings.prices.PricesRepo
import pl.srw.billcalculator.data.settings.prices.ProviderSettings
import pl.srw.billcalculator.data.settings.prices.TariffProviderSettings
import pl.srw.billcalculator.settings.details.dialog.SettingsTitleDescriptionMatcher
import pl.srw.billcalculator.type.Provider
import timber.log.Timber

class SettingsDetailsVM(private val pricesRepo: PricesRepo) : ViewModel() {

    val items = ObservableArrayList<SettingsDetailsListItem>()

    private var provider: Provider? = null // view model has activity scope
    private val settingsChangesObserver = Observer<ProviderSettings> { transform(it!!) }

    fun listItemsFor(newProvider: Provider) {
        provider?.let { unregisterObserver(it) }
        provider = newProvider
        registerObserver(newProvider)
    }

    fun valueChanged(title: String, value: String, enabled: Boolean) {
        Timber.i("Settings details: $title value changed to: $value and enabled=$enabled")
        pricesRepo.updatePrice(provider!!, title, value.autoCorrect()) // TODO: pass enabled
    }

    fun optionPicked(titleResId: Int, valueResId: Int) {
        for (item in items) {
            if (item is PickingSettingsDetailsListItem
                && item.title == titleResId
            ) {
                if (item.value != valueResId) pricesRepo.updateTariff(provider!!, EnergyTariff.findByStringRes(valueResId))
                return
            }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    public override fun onCleared() {
        unregisterObserver(provider!!)
    }

    private fun transform(data: ProviderSettings) {
        Timber.d("Setting settings details list")
        val newItems = mutableListOf<SettingsDetailsListItem>()
        if (data is TariffProviderSettings) {
            newItems += PickingSettingsDetailsListItem(
                title = provider!!.tariffResource,
                value = data.tariff.stringRes,
                options = EnergyTariff.allStringResources()
            )
        }
        for ((title, price) in data.prices) {
            newItems += InputSettingsDetailsListItem(
                title = title,
                optional = price is OptionalPriceValue,
                enabled = (price as? OptionalPriceValue)?.enabled ?: true,
                value = price.value,
                measure = price.measure.resId,
                description = SettingsTitleDescriptionMatcher.mapping[title]
            )
        }
        items.clear()
        items.addAll(newItems)
    }

    private fun registerObserver(newProvider: Provider) {
        Timber.d("Getting settings details list for $newProvider")
        when (newProvider) {
            Provider.PGE -> pricesRepo.pgeSettings.observeForever(settingsChangesObserver)
            Provider.PGNIG -> pricesRepo.pgnigSettings.observeForever(settingsChangesObserver)
            Provider.TAURON -> pricesRepo.tauronSettings.observeForever(settingsChangesObserver)
        }
    }

    private fun unregisterObserver(oldProvider: Provider) {
        when (oldProvider) {
            Provider.PGE -> pricesRepo.pgeSettings.removeObserver(settingsChangesObserver)
            Provider.PGNIG -> pricesRepo.pgnigSettings.removeObserver(settingsChangesObserver)
            Provider.TAURON -> pricesRepo.tauronSettings.removeObserver(settingsChangesObserver)
        }
    }
}

private fun String.autoCorrect(): String {
    return if (this.isBlank() || "0.0".contains(this)) "0.00"
    else if (this.startsWith('.')) "0$this"
    else this
}
