package pl.srw.billcalculator.data.settings.prices

import android.arch.lifecycle.MutableLiveData
import pl.srw.billcalculator.settings.prices.RestorablePrices
import pl.srw.billcalculator.settings.prices.SharedPreferencesEnergyPrices
import pl.srw.billcalculator.type.EnumVariantNotHandledException
import pl.srw.billcalculator.type.Provider
import pl.srw.billcalculator.util.ProviderMapper
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PricesRepo @Inject constructor(private val providerMapper: ProviderMapper) {

    val tariffPge = MutableLiveData<String>()
    val tariffTauron = MutableLiveData<String>()

    init {
        tariffPge.value = getTariff(Provider.PGE)
        tariffTauron.value = getTariff(Provider.TAURON)
        Timber.d("PGE tariff = ${tariffPge.value}, TAURON tariff = ${tariffTauron.value}")
    }

    fun getPrices(provider: Provider): RestorablePrices = providerMapper.getPrices(provider)

    @SharedPreferencesEnergyPrices.TariffOption
    fun getTariff(provider: Provider) = (getPrices(provider) as SharedPreferencesEnergyPrices).tariff

    fun updateTariff(provider: Provider, tariff: String) {
        Timber.d("Upgrading $provider tariff to $tariff")
        when (provider) {
            Provider.PGE -> tariffPge.value = tariff
            Provider.TAURON -> tariffTauron.value = tariff
            else -> throw EnumVariantNotHandledException(provider)
        }
    }
}
