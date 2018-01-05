package pl.srw.billcalculator.settings.fragment;

import android.app.Dialog;
import android.preference.EditTextPreference;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import javax.inject.Inject;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.di.Dependencies;
import pl.srw.billcalculator.settings.prices.PgnigPrices;
import pl.srw.billcalculator.type.Provider;

public class PgnigSettingsFragment extends ProviderSettingsFragment {

    @Inject PgnigPrices pgnigPrices;

    @Override
    public void init() {
        super.init();
        setWspKonwersjiDescription();
    }

    @Override
    protected int getPreferencesResource() {
        return R.xml.pgnig_preferences;
    }

    @Override
    protected Provider getProvider() {
        return Provider.PGNIG;
    }

    private void setWspKonwersjiDescription() {
        EditTextPreference wspKonwersjiPreference = (EditTextPreference) findPreference(getString(R.string.preferences_pgnig_wsp_konwersji));
        wspKonwersjiPreference.setDialogMessage(Html.fromHtml(getString(R.string.wsp_konwersji_desc)));
        wspKonwersjiPreference.setOnPreferenceClickListener(preference -> {
            Dialog dialog = ((EditTextPreference) preference).getDialog();
            TextView tvDesc = (TextView) dialog.findViewById(android.R.id.message);
            tvDesc.setMovementMethod(LinkMovementMethod.getInstance());
            return false;
        });
    }

    @Override
    protected String getMeasure(final String key) {
        if (getString(R.string.preferences_pgnig_wsp_konwersji).equals(key)) {
            return "";
        }
        return super.getMeasure(key);
    }

    @Override
    protected String getMonthMeasurePrefKeys() {
        return getStringFor(R.string.preferences_pgnig_abonamentowa,
                R.string.preferences_pgnig_dystrybucyjna_stala);
    }

    @Override
    protected String getMWhMeasurePrefKeys() {
        return "";
    }

    @Override
    protected void injectDependencies() {
        Dependencies.getApplicationComponent().getSettingsComponent().inject(this);
    }
}
