package pl.srw.billcalculator.settings.fragment;

import android.content.SharedPreferences;

import javax.inject.Inject;

import pl.srw.billcalculator.data.settings.PricesRepo;

abstract class EnergyProviderSettingsFragment extends ProviderSettingsFragment {

    @Inject PricesRepo pricesRepo;

    private int preferencesSchemaResId;

    protected abstract String getTariffKey();

    protected abstract boolean isTariffG12();

    protected abstract int getPreferencesG11Resource();

    protected abstract int getPreferencesG12Resource();

    @Override
    protected final int getPreferencesResource() {
        return preferencesSchemaResId;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        super.onSharedPreferenceChanged(sharedPreferences, key);

        if (key.equals(getTariffKey())) {
            refreshScreen();
            String tariff = sharedPreferences.getString(key, null);
            pricesRepo.updateTariff(getProvider(), tariff);
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
