package pl.srw.billcalculator.data.settings.prices

import android.arch.lifecycle.MutableLiveData
import pl.srw.billcalculator.settings.prices.SharedPreferencesEnergyPrices
import pl.srw.billcalculator.type.EnumVariantNotHandledException
import pl.srw.billcalculator.type.Provider
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PricesRepo @Inject constructor(private val pricesBridge: PricesBridge) {

    val tariffPge = MutableLiveData<String>() // TODO: replace with EnergyTariff
    val tariffTauron = MutableLiveData<String>()

    init {
        tariffPge.value = getTariff(Provider.PGE)
        tariffTauron.value = getTariff(Provider.TAURON)
        Timber.d("PGE tariff = ${tariffPge.value}, TAURON tariff = ${tariffTauron.value}")
    }

    fun getProviderSettings(provider: Provider): ProviderSettings = when(provider) {
        Provider.PGNIG -> SimpleProviderSettings(Provider.PGNIG, pricesBridge.getItemsForPgnig())
        Provider.PGE -> {
            if (SharedPreferencesEnergyPrices.TARIFF_G11 == tariffPge.value)
                TariffProviderSettings(Provider.PGE, EnergyTariff.G11, pricesBridge.getItemsForPgeG11())
            else TariffProviderSettings(Provider.PGE, EnergyTariff.G12, pricesBridge.getItemsForPgeG12())
        }
        Provider.TAURON -> {
            if (SharedPreferencesEnergyPrices.TARIFF_G11 == tariffTauron.value)
                TariffProviderSettings(Provider.TAURON, EnergyTariff.G11, pricesBridge.getItemsForTauronG11())
            else TariffProviderSettings(Provider.TAURON, EnergyTariff.G12, pricesBridge.getItemsForTauronG12())
        }
    }

    fun updateTariff(provider: Provider, tariff: String) {
        Timber.d("Upgrading $provider tariff to $tariff")
        when (provider) {
            Provider.PGE -> tariffPge.value = tariff
            Provider.TAURON -> tariffTauron.value = tariff
            else -> throw EnumVariantNotHandledException(provider)
        }
    }

    @SharedPreferencesEnergyPrices.TariffOption
    private fun getTariff(provider: Provider) = when(provider) {
        Provider.PGE -> pricesBridge.getPgeTariff()
        Provider.TAURON -> pricesBridge.getTauronTariff()
        else -> throw EnumVariantNotHandledException(provider)
    }
}
