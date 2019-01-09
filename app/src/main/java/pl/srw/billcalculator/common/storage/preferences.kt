package pl.srw.billcalculator.common.storage

import android.content.SharedPreferences
import pl.srw.billcalculator.data.settings.prices.EnergyTariff
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Delegates [String] property getter and setter to Shared Preferences associated with give key
 */
class SharedPreferencesStringProperty(private val key: String,
                                      private val prefs: SharedPreferences): ReadWriteProperty<Any?, String> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): String =
            prefs[key]

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        prefs[key] = value
    }
}
/**
 * Delegates [Boolean] property getter and setter to Shared Preferences associated with give key
 */
class SharedPreferencesBooleanProperty(
    private val key: String,
    private val prefs: SharedPreferences
): ReadWriteProperty<Any?, Boolean> {

    override fun getValue(thisRef: Any?, property: KProperty<*>): Boolean =
            prefs[key]

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) {
        prefs[key] = value
    }
}

/**
 * Delegates [EnergyTariff] property getter and setter to Shared Preferences associated with give key
 */
class SharedPreferencesTariffProperty(private val key: String,
                                      private val prefs: SharedPreferences): ReadWriteProperty<Any?, EnergyTariff> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): EnergyTariff {
        val value: String = prefs[key, EnergyTariff.G11.name]
        return EnergyTariff.valueOf(value)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: EnergyTariff) {
        prefs[key] = value.name
    }
}

/**
 * Helper methods for shared prefs manipulation
 * Sample:
 * ```
 *     val prefs = ...
 *     prefs[Consts.SharedPrefs.KEY] = "any_type_of_value" //setter
 *     val value: String? = prefs[Consts.SharedPrefs.KEY] //getter
 *     val anotherValue: Int? = prefs[Consts.SharedPrefs.KEY, 10] //getter with default value
 * ```
 * Inspired by: https://medium.com/@krupalshah55/manipulating-shared-prefs-with-kotlin-just-two-lines-of-code-29af62440285
 */

/**
 * puts a key value pair in shared prefs if doesn't exists, otherwise updates value on given [key]
 */
operator fun SharedPreferences.set(key: String, value: Any?) {
    when (value) {
        is String? -> edit { it.putString(key, value) }
        is Int -> edit { it.putInt(key, value) }
        is Boolean -> edit { it.putBoolean(key, value) }
        is Float -> edit { it.putFloat(key, value) }
        is Long -> edit { it.putLong(key, value) }
        else -> throw UnsupportedOperationException("Not yet implemented")
    }
}

/**
 * finds value on given key.
 * [T] is the type of value
 * @param defaultValue optional default value - will take null for strings,
 * false for bool and 0 for numeric values if [defaultValue] is not specified
 */
inline operator fun <reified T : Any> SharedPreferences.get(key: String, defaultValue: T? = null): T {
    return when (T::class) {
        String::class -> getString(key, defaultValue as? String) as T
        Int::class -> getInt(key, defaultValue as? Int ?: 0) as T
        Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T
        Float::class -> getFloat(key, defaultValue as? Float ?: 0f) as T
        Long::class -> getLong(key, defaultValue as? Long ?: 0) as T
        else -> throw UnsupportedOperationException("Not yet implemented")
    }
}

/**
 * removes value for given key
 */
fun SharedPreferences.remove(key: String) {
    edit { it.remove(key) }
}

private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
    val editor = this.edit()
    operation(editor)
    editor.apply()
}
