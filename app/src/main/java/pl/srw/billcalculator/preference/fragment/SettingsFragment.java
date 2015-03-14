package pl.srw.billcalculator.preference.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.preference.activity.ProviderSettingsActivity;

/**
 * Created by Kamil Seweryn
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen prefScreen, Preference pref) {
        super.onPreferenceTreeClick(prefScreen, pref);
        if (pref.getKey().equals(getString(R.string.preferences_pge_prices))) {
            final Intent intent = ProviderSettingsActivity
                    .createIntent(getActivity(), ProviderSettingsActivity.Provider.PGE);
            startActivity(intent);
            return true;

        } else if (pref.getKey().equals(getString(R.string.preferences_pgnig_prices))) {
            final Intent intent = ProviderSettingsActivity
                    .createIntent(getActivity(), ProviderSettingsActivity.Provider.PGNIG);
            startActivity(intent);
            return true;
        }
        return false;
    }
}
