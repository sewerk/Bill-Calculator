package pl.srw.billcalculator.data.settings

import pl.srw.billcalculator.type.Provider

sealed class GlobalSettingsElement

data class ProviderSettingsElement(val provider: Provider) : GlobalSettingsElement()
