package pl.srw.billcalculator.preference;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.SettingsActivity;

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
            // Display the fragment as the main content.
            getFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, new PgeSettingsFragment(), SettingsActivity.FRAGMENT_TAG)
                    .addToBackStack(null)
                    .commit();
            return true;

        } else if (pref.getKey().equals(getString(R.string.preferences_pgnig_prices))) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, new PgnigSettingsFragment(), SettingsActivity.FRAGMENT_TAG)
                    .addToBackStack(null)
                    .commit();
            return true;
        }
        return false;
    }
}
