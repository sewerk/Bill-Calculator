package pl.srw.billcalculator.settings.fragment;

import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import pl.srw.billcalculator.R;

/**
 * Created by Kamil Seweryn.
 */
public abstract class EnergyProviderSettingsFragment extends ProviderSettingsFragment {

    public static final String TARIFF_G11 = "G11";
    public static final String TARIFF_G12 = "G12";

    @StringDef({TARIFF_G11, TARIFF_G12})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TariffOption {}

    @Override
    public int getHelpLayoutResource() {
        return R.layout.energy_settings_help;
    }

    protected abstract String getTariffKey();

    @Override
    public void init() {
        super.init();
        changePreferenceVisibilityDependingOnTaryfa();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        super.onSharedPreferenceChanged(sharedPreferences, key);

        if (key.equals(getTariffKey())) {
            changePreferenceVisibilityDependingOnTaryfa();
        }
    }

    private void changePreferenceVisibilityDependingOnTaryfa() {
        ListPreference taryfaPreference = (ListPreference) findPreference(getTariffKey());
        if (isPickedValue(taryfaPreference, TARIFF_G12)) {
            findPreference(getString(R.string.preferences_category_G11)).setEnabled(false);
            findPreference(getString(R.string.preferences_energy_category_G12)).setEnabled(true);
        } else {
            findPreference(getString(R.string.preferences_category_G11)).setEnabled(true);
            findPreference(getString(R.string.preferences_energy_category_G12)).setEnabled(false);
        }
    }

    private boolean isPickedValue(final ListPreference preference, @TariffOption final String tariff) {
        return preference.getValue().equals(tariff);
    }

    @Override
    protected void restoreSettings() {
        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .edit().putString(getTariffKey(), TARIFF_G11)
                .apply();
    }
}
