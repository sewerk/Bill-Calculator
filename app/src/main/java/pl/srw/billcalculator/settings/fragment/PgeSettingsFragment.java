package pl.srw.billcalculator.settings.fragment;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.settings.prices.PgePrices;

/**
 * Created by Kamil Seweryn.
 */
public class PgeSettingsFragment extends EnergyProviderSettingsFragment {

    @Override
    protected int getPreferencesResource() {
        return R.xml.pge_preferences;
    }

    @Override
    public int getHelpImageExampleResource() {
        return R.drawable.pge_example;
    }

    @Override
    public int getTitleResource() {
        return R.string.pge_prices;
    }

    @Override
    protected String getTariffKey() {
        return getString(R.string.preferences_pge_tariff);
    }

    @Override
    protected String getMonthMeasurePrefKeys() {
        return getStringFor(R.string.preferences_pge_oplata_przejsciowa,
                R.string.preferences_pge_oplata_stala_za_przesyl,
                R.string.preferences_pge_oplata_abonamentowa);
    }

    @Override
    public void restoreSettings() {
        super.restoreSettings();
        new PgePrices().setDefault();
    }
}
