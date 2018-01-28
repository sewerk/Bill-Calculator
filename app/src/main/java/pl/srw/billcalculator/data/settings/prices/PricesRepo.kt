package pl.srw.billcalculator.data.settings.prices

import android.arch.lifecycle.LiveData
import pl.srw.billcalculator.type.Provider

interface PricesRepo {

    val tariffPge: LiveData<EnergyTariff>
    val tariffTauron: LiveData<EnergyTariff>
    val pgeSettings: LiveData<ProviderSettings>
    val pgnigSettings: LiveData<ProviderSettings>
    val tauronSettings: LiveData<ProviderSettings>

    fun setDefaultPricesFor(provider: Provider)

    fun updateTariff(provider: Provider, tariff: EnergyTariff)

    fun updatePrice(provider: Provider, priceName: String, priceValue: String)
}
