package pl.srw.billcalculator.settings.prices

import android.content.SharedPreferences
import pl.srw.billcalculator.common.storage.SharedPreferencesBooleanProperty
import pl.srw.billcalculator.common.storage.SharedPreferencesStringProperty
import pl.srw.billcalculator.common.storage.SharedPreferencesTariffProperty

abstract class SharedPrefsPrices(private val prefs: SharedPreferences) {
    protected fun stringPref(key: String) = SharedPreferencesStringProperty(key, prefs)
    protected fun booleanPref(key: String) = SharedPreferencesBooleanProperty(key, prefs)
    protected fun tariffPref(key: String) = SharedPreferencesTariffProperty(key, prefs)
}
