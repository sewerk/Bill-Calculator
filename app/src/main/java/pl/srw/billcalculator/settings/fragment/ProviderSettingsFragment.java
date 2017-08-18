package pl.srw.billcalculator.settings.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.Map;

import hugo.weaving.DebugLog;
import pl.srw.billcalculator.BackableActivity;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.settings.help.ProviderSettingsHelpActivity;
import pl.srw.billcalculator.settings.restore.ConfirmRestoreSettingsDialogFragment;
import pl.srw.billcalculator.type.EnumVariantNotHandledException;
import pl.srw.billcalculator.type.Provider;
import pl.srw.billcalculator.wrapper.Analytics;

public abstract class ProviderSettingsFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG_CONFIRM_RESTORE = "RESTORE_DEFAULT_CONFIRM_DIALOG";
    private static final String EMPTY_VALUE_REPLACEMENT = "0.00";

    /**
     * Creates new provider settings fragment
     * @param provider  provider type
     * @return new instance of {@link ProviderSettingsFragment}
     */
    public static ProviderSettingsFragment newInstance(Provider provider) {
        switch (provider) {
            case PGNIG:
                return new PgnigSettingsFragment();
            case PGE:
                return new PgeSettingsFragment();
            case TAURON:
                return new TauronSettingsFragment();
        }
        throw new EnumVariantNotHandledException(provider);
    }

    @DebugLog
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependencies();
        init();
        setHasOptionsMenu(true);
    }

    protected abstract void injectDependencies();

    protected void init() {
        addPreferencesFromResource(getPreferencesResource());
        setSummary();
    }

    protected abstract Provider getProvider();

    protected abstract int getPreferencesResource();

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.settings, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_help) {
            showHelp();
            return true;
        } else if (item.getItemId() == R.id.action_default) {
            Analytics.log("Restore prices clicked for " + getProvider());
            ConfirmRestoreSettingsDialogFragment.newInstance(getProvider())
                    .show(((BackableActivity) getActivity()).getSupportFragmentManager(), TAG_CONFIRM_RESTORE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        replaceEmptyValue(sharedPreferences, key);
        updateSummary(sharedPreferences, key);
    }

    public void refreshScreen() {
        setPreferenceScreen(null);
        init();
    }

    private void showHelp() {
        final Intent intent = ProviderSettingsHelpActivity.createIntent(getActivity(), getProvider());
        startActivity(intent);
    }

    private void replaceEmptyValue(final SharedPreferences sharedPreferences, final String key) {
        if (TextUtils.isEmpty(sharedPreferences.getString(key, EMPTY_VALUE_REPLACEMENT)))
            sharedPreferences.edit().putString(key, EMPTY_VALUE_REPLACEMENT).apply();
    }

    private void setSummary() {
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        Map<String, ?> preferences = sharedPreferences.getAll();
        for (String key : preferences.keySet()) {
            updateSummary(sharedPreferences, key);
        }
    }

    private void updateSummary(final SharedPreferences sharedPreferences, final String key) {
        Preference preference = findPreference(key);
        if (preference instanceof EditTextPreference) {
            preference.setSummary(sharedPreferences.getString(key, EMPTY_VALUE_REPLACEMENT) + " " + getMeasure(key));

        } else if (preference instanceof ListPreference) {
            final String value = ((ListPreference) preference).getValue();
            final int indexOfValue = ((ListPreference) preference).findIndexOfValue(value);
            preference.setSummary(getResources().getStringArray(R.array.energy_tariff_picks)[indexOfValue]);
        }
    }

    protected String getMeasure(final String key) {
        if (getMonthMeasurePrefKeys().contains(key)) {
            return "[zł/m-c]";
        } else if (getMWhMeasurePrefKeys().contains(key)) {
            return "[zł/MWh]";
        }
        return "[zł/kWh]";
    }

    protected abstract String getMonthMeasurePrefKeys();

    protected abstract String getMWhMeasurePrefKeys();

    protected String getStringFor(@StringRes final int... keys) {
        StringBuilder sb = new StringBuilder();
        for (int i : keys) {
            sb.append(getString(i));
        }
        return sb.toString();
    }
}
