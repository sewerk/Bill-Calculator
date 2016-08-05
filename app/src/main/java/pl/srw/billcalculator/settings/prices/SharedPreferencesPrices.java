package pl.srw.billcalculator.settings.prices;

import android.content.SharedPreferences;

public abstract class SharedPreferencesPrices implements RestorablePrices {

    private final SharedPreferences __prefs;

    protected SharedPreferencesPrices(SharedPreferences prefs) {
        this.__prefs = prefs;
    }

    protected String getPref(String key, String defValue) {
        return this.__prefs.getString(key, defValue);
    }

    protected void setPref(String key, String value) {
        this.__prefs.edit().putString(key, value).apply();
    }

    protected boolean containsPref(String key) {
        return this.__prefs.contains(key);
    }

    protected void removePref(String key) {
        this.__prefs.edit().remove(key).apply();
    }
}
