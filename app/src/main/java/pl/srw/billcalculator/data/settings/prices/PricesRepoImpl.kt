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
        debugMeasure("Retrieving prices data from shared prefs") {
            for (provider in Provider.values()) {
                refreshTariff(provider)
                refreshProviderSettings(provider)
            }
        }
        Timber.i("PGE tariff = ${tariffPge.value}, TAURON tariff = ${tariffTauron.value}")
    }

    override fun setDefaultPricesFor(provider: Provider) {
        Timber.d("Setting defaults for $provider")
        pricesBridge.setDefaults(provider)
        refreshTariff(provider)
        refreshProviderSettings(provider)
    }

    override fun updateTariff(provider: Provider, tariff: EnergyTariff) {
        checkEnergyProvider(provider)
        Timber.i("Upgrading $provider tariff to $tariff")
        pricesBridge.updateTariff(provider, tariff)
        refreshTariff(provider)
        refreshProviderSettings(provider)
    }

    override fun updatePrice(provider: Provider, priceName: String, priceValue: String) {
        Timber.d("Updating $provider price: $priceName = $priceValue")
        when (provider) {
            Provider.PGE -> pricesBridge.updatePge(priceName, priceValue)
            Provider.PGNIG -> pricesBridge.updatePgnig(priceName, priceValue)
            Provider.TAURON -> pricesBridge.updateTauron(priceName, priceValue)
        }
        refreshProviderSettings(provider)
    }

    private fun refreshTariff(provider: Provider) {
        when (provider) {
            Provider.PGE -> tariffPge.value = pricesBridge.getTariff(Provider.PGE)
            Provider.TAURON -> tariffTauron.value = pricesBridge.getTariff(Provider.TAURON)
            else -> Timber.w("Refresh tariff for $provider invoked")
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

    private fun checkEnergyProvider(provider: Provider) {
        if (!isEnergy(provider)) throw EnumVariantNotHandledException(provider)
    }

    private fun isEnergy(provider: Provider) = provider == Provider.PGE || provider == Provider.TAURON
}
