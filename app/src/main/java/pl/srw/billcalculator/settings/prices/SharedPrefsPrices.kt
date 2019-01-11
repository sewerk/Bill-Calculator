package pl.srw.billcalculator.settings.prices

import android.content.SharedPreferences
import pl.srw.billcalculator.common.storage.SharedPreferencesBooleanProperty
import pl.srw.billcalculator.common.storage.SharedPreferencesStringProperty
import pl.srw.billcalculator.common.storage.SharedPreferencesTariffProperty
import pl.srw.billcalculator.db.DISABLED_OPLATA_HANDLOWA

abstract class SharedPrefsPrices(private val prefs: SharedPreferences) {

    abstract var oplataHandlowa: String
    abstract var enabledOplataHandlowa: Boolean

    protected val oplataHandlowaForDb
        get() = if (enabledOplataHandlowa) oplataHandlowa else DISABLED_OPLATA_HANDLOWA

    protected fun stringPref(key: String) = SharedPreferencesStringProperty(key, prefs)
    protected fun booleanPref(key: String) = SharedPreferencesBooleanProperty(key, prefs)
    protected fun tariffPref(key: String) = SharedPreferencesTariffProperty(key, prefs)
}
