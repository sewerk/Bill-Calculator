package pl.srw.billcalculator;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import java.util.Map;

/**
 * Created by Kamil Seweryn
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final int[] G12_DZIENNA_PREFERENCES = {
            R.string.preferences_za_energie_czynna_G12dzien,
            R.string.preferences_skladnik_jakosciowy_G12dzien,
            R.string.preferences_oplata_sieciowa_G12dzien
    };

    public static final int[] G12_NOCNA_PREFERENCES = {
            R.string.preferences_za_energie_czynna_G12noc,
            R.string.preferences_skladnik_jakosciowy_G12noc,
            R.string.preferences_oplata_sieciowa_G12noc
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        addPrefixForG12();
        changePreferenceVisibilityDependingOnTaryfa();
        setWspKonwersjiDescription();
        setSummary();
    }

    private void addPrefixForG12() {
        for (int prefKeyId : G12_DZIENNA_PREFERENCES) {
            findPreference(getString(prefKeyId)).setTitle(findPreference(getString(prefKeyId)).getTitle() + getString(R.string.postfix_strefa_dzienna));
        }
        for (int prefKeyId : G12_NOCNA_PREFERENCES) {
            findPreference(getString(prefKeyId)).setTitle(findPreference(getString(prefKeyId)).getTitle() + getString(R.string.postfix_strefa_nocna));
        }
    }

    private void changePreferenceVisibilityDependingOnTaryfa() {
        CheckBoxPreference taryfaPreferece = (CheckBoxPreference) findPreference(getString(R.string.preferences_taryfa_dwustrefowa));
        if (taryfaPreferece.isChecked()) {
            findPreference(getString(R.string.preferences_G11)).setEnabled(false);
            findPreference(getString(R.string.preferences_G12)).setEnabled(true);
        } else {
            findPreference(getString(R.string.preferences_G11)).setEnabled(true);
            findPreference(getString(R.string.preferences_G12)).setEnabled(false);
        }
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
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updateSummary(sharedPreferences, key);
        changeTaryfa(sharedPreferences, key);
    }

    private void updateSummary(final SharedPreferences sharedPreferences, final String key) {
        Preference preference = findPreference(key);
        if (preference instanceof EditTextPreference) {
            preference.setSummary(sharedPreferences.getString(key, "0.0"));
        }
    }

    private void changeTaryfa(final SharedPreferences sharedPreferences, final String key) {
        if (key.equals(getString(R.string.preferences_taryfa_dwustrefowa))) {
            changePreferenceVisibilityDependingOnTaryfa();
        }
    }
}
