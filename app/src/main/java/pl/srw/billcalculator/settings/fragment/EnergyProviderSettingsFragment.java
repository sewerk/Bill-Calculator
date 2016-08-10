package pl.srw.billcalculator.settings.fragment;

import android.content.SharedPreferences;

import javax.inject.Inject;

import pl.srw.billcalculator.R;

/**
 * Created by Kamil Seweryn.
 */
abstract class EnergyProviderSettingsFragment extends ProviderSettingsFragment {

    @Inject SharedPreferences prefs;

    private int preferencesSchemaResId;

    protected abstract String getTariffKey();

    protected abstract boolean isTariffG12();

    protected abstract int getPreferencesG11Resource();

    protected abstract int getPreferencesG12Resource();

    @Override
    public int getHelpLayoutResource() {
        return R.layout.settings_help_energy;
    }

    @Override
    protected final int getPreferencesResource() {
        return preferencesSchemaResId;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        super.onSharedPreferenceChanged(sharedPreferences, key);

        if (key.equals(getTariffKey())) {
            setPreferenceScreen(null);
            init();
        }
    }

    @Override
    public void init() {
        switchTariffPreferenceSchema();
        super.init();
    }

    private void switchTariffPreferenceSchema() {
        if (isTariffG12()) {
            preferencesSchemaResId = getPreferencesG12Resource();
        } else {
            preferencesSchemaResId = getPreferencesG11Resource();
        }
    }
}
