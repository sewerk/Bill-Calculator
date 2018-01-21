package pl.srw.billcalculator.settings.list

import android.arch.lifecycle.ViewModel
import android.os.Bundle
import pl.srw.billcalculator.data.settings.GlobalSettingsElement
import pl.srw.billcalculator.data.settings.ProviderSettingsElement
import pl.srw.billcalculator.data.settings.SettingsRepo
import pl.srw.billcalculator.type.Provider
import pl.srw.billcalculator.util.SingleLiveEvent
import pl.srw.billcalculator.util.StateRestorable
import timber.log.Timber

private const val STATE_SELECTED_INDEX = "settings.state.selectedIndex"

class SettingsVM(private val settingsRepo: SettingsRepo) : ViewModel(), StateRestorable {

    val switchProviderTab = SingleLiveEvent<Provider>()
    val openProviderSettings = SingleLiveEvent<Provider>()
    val items: List<SettingsListItem> = settingsRepo.globalList().map(this::convert)

    var selectedIndex = 0
        private set
    var isOnTablet: Boolean = false
        set(value) {
            Timber.d("settings isOnTablet flag")
            field = value
            if (value) onRowClicked(selectedIndex)
        }

    fun onRowClicked(position: Int) {
        selectedIndex = position
        val provider = getProvider(position) // FIXME: make more generic
        Timber.i("Settings: $provider picked")
        if (isOnTablet) switchProviderTab.value = provider
        else openProviderSettings.value = provider
    }

    override fun writeTo(bundle: Bundle) {
        bundle.putInt(STATE_SELECTED_INDEX, selectedIndex)
    }

    override fun readFrom(bundle: Bundle) {
        selectedIndex = bundle.getInt(STATE_SELECTED_INDEX)
    }

    private fun getProvider(position: Int): Provider {
        return items[position].provider
    }

    private fun convert(element: GlobalSettingsElement): SettingsListItem =
            when(element) {
                is ProviderSettingsElement ->
                    element.provider.let { SettingsListItem(it, it.settingsTitleRes, it.settingsDescRes, it.logoSmallRes) }
            }
}
