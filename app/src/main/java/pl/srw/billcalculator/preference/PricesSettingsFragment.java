package pl.srw.billcalculator.preference;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.text.TextUtils;

import java.util.Map;

import pl.srw.billcalculator.R;

/**
 * Created by Kamil Seweryn.
 */
public abstract class PricesSettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String EMPTY_VALUE_REPLACEMENT = "0.0";

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(getPreferencesResource());
        setSummary();
    }

    protected abstract int getPreferencesResource();

    private void setSummary() {
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        Map<String, ?> preferences = sharedPreferences.getAll();
        for (String key : preferences.keySet()) {
            updateSummary(sharedPreferences, key);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        replaceEmptyValue(sharedPreferences, key);
        updateSummary(sharedPreferences, key);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    private void replaceEmptyValue(final SharedPreferences sharedPreferences, final String key) {
        if (TextUtils.isEmpty(sharedPreferences.getString(key, EMPTY_VALUE_REPLACEMENT)))
            sharedPreferences.edit().putString(key, EMPTY_VALUE_REPLACEMENT).commit();
    }

    private void updateSummary(final SharedPreferences sharedPreferences, final String key) {
        Preference preference = findPreference(key);
        if (preference instanceof EditTextPreference) {
            preference.setSummary(sharedPreferences.getString(key, EMPTY_VALUE_REPLACEMENT) + " " + getMeasure(key));

        } else if (preference instanceof ListPreference) {
            final String value = ((ListPreference) preference).getValue();
            final int indexOfValue = ((ListPreference) preference).findIndexOfValue(value);
            preference.setSummary(getResources().getStringArray(R.array.pge_tariff_picks)[indexOfValue]);
        }
    }

    protected String getMeasure(final String key) {
        if (getMonthMeasurePrefKeys().indexOf(key) > -1) {
            return "[zł/m-c]";
        }
        return "[zł/kWh]";
    }

    protected abstract String getMonthMeasurePrefKeys();

    protected String getStringFor(final int... keys) {
        StringBuilder sb = new StringBuilder();
        for (int i : keys) {
            sb.append(getString(i));
        }
        return sb.toString();
    }
}
