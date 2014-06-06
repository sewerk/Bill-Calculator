package pl.srw.billcalculator;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;

import java.util.Map;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

 @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        setSummary();
    }

    private void setSummary() {
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        Map<String, ?> preferences = sharedPreferences.getAll();
        for (String key : preferences.keySet()) {
            EditTextPreference preference = (EditTextPreference) findPreference(key);
            preference.setSummary(sharedPreferences.getString(key, "0.0"));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        EditTextPreference preference = (EditTextPreference) findPreference(key);
        preference.setSummary(sharedPreferences.getString(key, "0.0"));
    }
}
