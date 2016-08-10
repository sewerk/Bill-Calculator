package pl.srw.billcalculator.settings.fragment;

import javax.inject.Inject;

import pl.srw.billcalculator.BillCalculator;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.settings.prices.TauronPrices;
import pl.srw.billcalculator.type.Provider;

/**
 * Created by Kamil Seweryn.
 */
public class TauronSettingsFragment extends EnergyProviderSettingsFragment {

    @Inject TauronPrices tauronPrices;

    @Override
    protected int getPreferencesG11Resource() {
        return R.xml.tauron_g11_preferences;
    }

    @Override
    protected int getPreferencesG12Resource() {
        return R.xml.tauron_g12_preferences;
    }

    @Override
    public int getHelpImageExampleResource() {
        return R.drawable.tauron_example;
    }

    @Override
    protected String getTariffKey() {
        return TauronPrices.KEY_TARIFF;
    }

    @Override
    protected boolean isTariffG12() {
        return tauronPrices.isTariffG12();
    }

    @Override
    protected Provider getProvider() {
        return Provider.TAURON;
    }

    @Override
    protected String getMonthMeasurePrefKeys() {
        return "oplataDystrybucyjnaStala" + "oplataPrzejsciowa" + "oplataAbonamentowa";
    }

    @Override
    protected void injectDependencies() {
        BillCalculator.get(getActivity()).getApplicationComponent().getSettingsComponent().inject(this);
    }
}
