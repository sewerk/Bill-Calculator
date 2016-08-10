package pl.srw.billcalculator.settings.fragment;

import javax.inject.Inject;

import pl.srw.billcalculator.BillCalculator;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.settings.prices.PgePrices;
import pl.srw.billcalculator.type.Provider;

/**
 * Created by Kamil Seweryn.
 */
public class PgeSettingsFragment extends EnergyProviderSettingsFragment {

    @Inject PgePrices pgePrices;

    @Override
    protected int getPreferencesG11Resource() {
        return R.xml.pge_g11_preferences;
    }

    @Override
    protected int getPreferencesG12Resource() {
        return R.xml.pge_g12_preferences;
    }

    @Override
    public int getHelpImageExampleResource() {
        return R.drawable.pge_example;
    }

    @Override
    protected String getTariffKey() {
        return PgePrices.KEY_TARIFF;
    }

    @Override
    protected boolean isTariffG12() {
        return pgePrices.isTariffG12();
    }

    @Override
    protected Provider getProvider() {
        return Provider.PGE;
    }

    @Override
    protected String getMonthMeasurePrefKeys() {
        return getStringFor(R.string.preferences_pge_oplata_przejsciowa,
                R.string.preferences_pge_oplata_stala_za_przesyl,
                R.string.preferences_pge_oplata_abonamentowa);
    }

    @Override
    protected void injectDependencies() {
        BillCalculator.get(getActivity()).getApplicationComponent().getSettingsComponent().inject(this);
    }
}
