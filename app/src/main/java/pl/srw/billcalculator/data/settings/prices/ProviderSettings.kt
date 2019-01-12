package pl.srw.billcalculator.data.settings.prices

import pl.srw.billcalculator.type.Provider

sealed class ProviderSettings {
    abstract val provider: Provider
    abstract val prices: Map<String, PriceValue>
}

data class SimpleProviderSettings(
    override val provider: Provider,
    override val prices: Map<String, PriceValue>
) : ProviderSettings()

/** Provider settings with tariff to control calculation flow */
data class TariffProviderSettings(
    override val provider: Provider,
    val tariff: EnergyTariff,
    override val prices: Map<String, PriceValue>
) : ProviderSettings()
