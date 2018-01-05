package pl.srw.billcalculator.settings.fragment;

import javax.inject.Inject;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.di.Dependencies;
import pl.srw.billcalculator.settings.prices.PgePrices;
import pl.srw.billcalculator.settings.prices.SharedPreferencesEnergyPrices;
import pl.srw.billcalculator.type.Provider;

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
    protected String getTariffKey() {
        return PgePrices.KEY_TARIFF;
    }

    @Override
    protected boolean isTariffG12() {
        return SharedPreferencesEnergyPrices.TARIFF_G12.equals(pgePrices.getTariff());
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
        Dependencies.getApplicationComponent().getSettingsComponent().inject(this);
    }

    protected String getMWhMeasurePrefKeys() {
        return getStringFor(R.string.preferences_pge_oplata_oze);
    }
}
