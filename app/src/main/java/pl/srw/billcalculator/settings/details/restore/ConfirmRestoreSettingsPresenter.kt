package pl.srw.billcalculator.settings.details.restore

import pl.srw.billcalculator.data.settings.prices.PricesRepo
import pl.srw.billcalculator.settings.details.SettingsDetailsVM
import pl.srw.billcalculator.type.Provider
import pl.srw.billcalculator.util.analytics.Analytics
import pl.srw.billcalculator.util.analytics.EventType
import javax.inject.Inject

class ConfirmRestoreSettingsPresenter @Inject constructor(private val pricesRepo: PricesRepo,
                                                          private val settingsDetailsVM: SettingsDetailsVM) {
    private lateinit var provider: Provider

    fun setup(provider: Provider) {
        this.provider = provider
    }

    fun onConfirmClicked() {
        Analytics.event(EventType.RESTORE_PRICES, "restored", true, "for", provider)
        pricesRepo.setDefaultPricesFor(provider)
        settingsDetailsVM.updateItemsFor(provider)
    }

    fun onCancelClicked(){
        Analytics.event(EventType.RESTORE_PRICES, "restored", false, "for", provider)
    }
}
