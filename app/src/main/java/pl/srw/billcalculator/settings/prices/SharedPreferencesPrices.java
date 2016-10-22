package pl.srw.billcalculator.settings.prices;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import pl.srw.billcalculator.BillCalculator;

abstract class SharedPreferencesPrices {
    private final SharedPreferences __prefs;

    protected SharedPreferencesPrices() {
        this.__prefs = PreferenceManager.getDefaultSharedPreferences(BillCalculator.context);
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
