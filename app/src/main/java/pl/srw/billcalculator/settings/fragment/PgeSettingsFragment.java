package pl.srw.billcalculator.settings.fragment;

import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.settings.prices.PgePrices;

/**
 * Created by Kamil Seweryn.
 */
public class PgeSettingsFragment extends ProviderSettingsFragment {

    public static final String TARIFF_G11 = "G11";
    public static final String TARIFF_G12 = "G12";

    @StringDef({TARIFF_G11, TARIFF_G12})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TariffOption {}

    @Override
    public void init() {
        super.init();
        changePreferenceVisibilityDependingOnTaryfa();
    }

    @Override
    protected int getPreferencesResource() {
        return R.xml.pge_preferences;
    }

    @Override
    public int getHelpLayoutResource() {
        return R.layout.pge_settings_help;
    }

    @Override
    public int getTitleResource() {
        return R.string.pge_prices;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        super.onSharedPreferenceChanged(sharedPreferences, key);
        changeTaryfa(sharedPreferences, key);
    }

    private void changeTaryfa(final SharedPreferences sharedPreferences, final String key) {
        if (key.equals(getString(R.string.preferences_pge_tariff))) {
            changePreferenceVisibilityDependingOnTaryfa();
        }
    }

    private void changePreferenceVisibilityDependingOnTaryfa() {
        ListPreference taryfaPreference = (ListPreference) findPreference(getString(R.string.preferences_pge_tariff));
        if (isPickedValue(taryfaPreference, TARIFF_G12)) {
            findPreference(getString(R.string.preferences_pge_category_G11)).setEnabled(false);
            findPreference(getString(R.string.preferences_pge_category_G12)).setEnabled(true);
        } else {
            findPreference(getString(R.string.preferences_pge_category_G11)).setEnabled(true);
            findPreference(getString(R.string.preferences_pge_category_G12)).setEnabled(false);
        }
    }

    private boolean isPickedValue(final ListPreference preference, @TariffOption final String tariff) {
        return preference.getValue().equals(tariff);
    }

    @Override
    protected String getMonthMeasurePrefKeys() {
        return getStringFor(R.string.preferences_pge_oplata_przejsciowa,
                R.string.preferences_pge_oplata_stala_za_przesyl,
                R.string.preferences_pge_oplata_abonamentowa);
    }

    @Override
    public void restoreSettings() {
        new PgePrices().setDefault();
        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .edit().putString(getActivity().getString(R.string.preferences_pge_tariff), PgeSettingsFragment.TARIFF_G11)
                .apply();
    }
}
