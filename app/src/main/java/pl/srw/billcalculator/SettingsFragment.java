package pl.srw.billcalculator;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import java.util.Map;

/**
 * Created by Kamil Seweryn
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        setWspKonwersjiDescription();
        setSummary();
    }

    private void setWspKonwersjiDescription() {
        EditTextPreference wspKonwersjiPreference = (EditTextPreference) findPreference(getString(R.string.preferences_wsp_konwersji));
        wspKonwersjiPreference.setDialogMessage(Html.fromHtml(getString(R.string.wsp_konwersji_desc)));
        wspKonwersjiPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Dialog dialog = ((EditTextPreference) preference).getDialog();
                TextView tvDesc = (TextView) dialog.findViewById(android.R.id.message);
                tvDesc.setMovementMethod(LinkMovementMethod.getInstance());
                return false;
            }
        });
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
