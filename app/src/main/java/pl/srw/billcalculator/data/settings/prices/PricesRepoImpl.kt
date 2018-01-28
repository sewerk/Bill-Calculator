package pl.srw.billcalculator.data.settings.prices

import android.arch.lifecycle.MutableLiveData
import pl.srw.billcalculator.type.EnumVariantNotHandledException
import pl.srw.billcalculator.type.Provider
import pl.srw.billcalculator.util.debugMeasure
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PricesRepoImpl @Inject constructor(private val pricesBridge: PricesBridge) : PricesRepo {

    override val tariffPge = MutableLiveData<EnergyTariff>()
    override val tariffTauron = MutableLiveData<EnergyTariff>()
    override val pgeSettings = MutableLiveData<ProviderSettings>()
    override val pgnigSettings = MutableLiveData<ProviderSettings>()
    override val tauronSettings = MutableLiveData<ProviderSettings>()

    init {
        tariffPge.value = getTariff(Provider.PGE)
        tariffTauron.value = getTariff(Provider.TAURON)
        Timber.d("PGE tariff = ${tariffPge.value}, TAURON tariff = ${tariffTauron.value}")
        debugMeasure("Retrieving prices data from shared prefs") {
            for (provider in Provider.values()) refreshProviderSettings(provider)
        }
    }

    override fun setDefaultPricesFor(provider: Provider) {
        Timber.d("Setting defaults for $provider")
        pricesBridge.setDefaults(provider)
        refreshProviderSettings(provider)
    }

    override fun updateTariff(provider: Provider, tariff: EnergyTariff) {
        Timber.d("Upgrading $provider tariff to $tariff")
        when (provider) {
            Provider.PGE -> tariffPge.value = tariff
            Provider.TAURON -> tariffTauron.value = tariff
            else -> throw EnumVariantNotHandledException(provider)
        }
    }

    override fun updatePrice(provider: Provider, priceName: String, priceValue: String) {
        Timber.d("Updating $provider price: $priceName = $priceValue")
        when (provider) {
            Provider.PGE -> {
                pricesBridge.updatePge(priceName, priceValue)
                refreshProviderSettings(Provider.PGE)
            }
            Provider.PGNIG -> {
                pricesBridge.updatePgnig(priceName, priceValue)
                refreshProviderSettings(Provider.PGNIG)
            }
            Provider.TAURON -> {
                pricesBridge.updateTauron(priceName, priceValue)
                refreshProviderSettings(Provider.TAURON)
            }
        }
    }

    private fun refreshProviderSettings(provider: Provider) {
        when (provider) {
            Provider.PGNIG ->
                pgnigSettings.value = SimpleProviderSettings(Provider.PGNIG, pricesBridge.getItemsForPgnig())
            Provider.PGE -> {
                pgeSettings.value = if (EnergyTariff.G11 == tariffPge.value)
                    TariffProviderSettings(Provider.PGE, EnergyTariff.G11, pricesBridge.getItemsForPgeG11())
                else TariffProviderSettings(Provider.PGE, EnergyTariff.G12, pricesBridge.getItemsForPgeG12())
            }
            Provider.TAURON -> {
                tauronSettings.value = if (EnergyTariff.G11 == tariffTauron.value)
                    TariffProviderSettings(Provider.TAURON, EnergyTariff.G11, pricesBridge.getItemsForTauronG11())
                else TariffProviderSettings(Provider.TAURON, EnergyTariff.G12, pricesBridge.getItemsForTauronG12())
            }
        }
    }

    private fun getTariff(provider: Provider) = when(provider) {
        Provider.PGE, Provider.TAURON -> pricesBridge.getTariff(provider)
        else -> throw EnumVariantNotHandledException(provider)
    }
}
