package pl.srw.billcalculator.settings.fragment;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.settings.prices.TauronPrices;

/**
 * Created by Kamil Seweryn.
 */
public class TauronSettingsFragment extends EnergyProviderSettingsFragment {

    @Override
    protected int getPreferencesResource() {
        return R.xml.tauron_preferences;
    }

    @Override
    public int getHelpImageExampleResource() {
        return R.drawable.tauron_example;
    }

    @Override
    public int getTitleResource() {
        return R.string.tauron_prices;
    }

    @Override
    protected String getTariffKey() {
        return "preferences_tauron_tariff";
    }

    @Override
    protected String getMonthMeasurePrefKeys() {
        return "oplataDystrybucyjnaStala" + "oplataPrzejsciowa" + "oplataAbonamentowa";
    }

    @Override
    public void restoreSettings() {
        super.restoreSettings();
        new TauronPrices().setDefault();
    }
}
