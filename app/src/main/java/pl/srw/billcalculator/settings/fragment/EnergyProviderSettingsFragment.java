package pl.srw.billcalculator.settings.fragment;

import android.content.SharedPreferences;
import android.support.annotation.CallSuper;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Inject;

import pl.srw.billcalculator.R;

/**
 * Created by Kamil Seweryn.
 */
abstract class EnergyProviderSettingsFragment extends ProviderSettingsFragment {

    protected static final String TARIFF_G11 = "G11";
    protected static final String TARIFF_G12 = "G12";

    @StringDef({TARIFF_G11, TARIFF_G12})
    @Retention(RetentionPolicy.SOURCE)
    protected @interface TariffOption {}

    @Inject SharedPreferences prefs;

    private int preferencesSchemaResId;

    protected abstract String getTariffKey();

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

    @Override
    @CallSuper
    protected void restoreSettings() {
        prefs.edit()
                .putString(getTariffKey(), TARIFF_G11)
                .apply();
    }

    private boolean isTariffG12() {
        return prefs.getString(getTariffKey(), TARIFF_G11).equals(TARIFF_G12);
    }
}
