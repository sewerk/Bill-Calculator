package pl.srw.billcalculator.wrapper

import android.arch.lifecycle.MutableLiveData
import pl.srw.billcalculator.settings.prices.RestorablePrices
import pl.srw.billcalculator.settings.prices.SharedPreferencesEnergyPrices
import pl.srw.billcalculator.type.EnumVariantNotHandledException
import pl.srw.billcalculator.type.Provider
import pl.srw.billcalculator.util.ProviderMapper
import javax.inject.Inject

class PricesRepo @Inject constructor(private val providerMapper: ProviderMapper) {

    val tariffPge = MutableLiveData<String>()
    val tariffTauron = MutableLiveData<String>()

    init {
        tariffPge.value = getTariff(Provider.PGE)
        tariffTauron.value = getTariff(Provider.TAURON)
    }

    fun getPrices(provider: Provider): RestorablePrices = providerMapper.getPrices(provider)

    @SharedPreferencesEnergyPrices.TariffOption
    fun getTariff(provider: Provider) = (getPrices(provider) as SharedPreferencesEnergyPrices).tariff

    fun updateTariff(provider: Provider, tariff: String) {
        when (provider) {
            Provider.PGE -> tariffPge.value = tariff
            Provider.TAURON -> tariffTauron.value = tariff
            else -> throw EnumVariantNotHandledException(provider)
        }
    }
}