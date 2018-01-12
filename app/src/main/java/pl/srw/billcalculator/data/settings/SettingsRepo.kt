package pl.srw.billcalculator.data.settings

import pl.srw.billcalculator.type.Provider
import javax.inject.Inject

class SettingsRepo @Inject constructor(){

    /**
     * Provides data for main settings screen
     */
    fun globalList(): Collection<GlobalSettingsElement> {
        return Provider.values().map { ProviderSettingsElement(it) }
    }
}
