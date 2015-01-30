package pl.srw.billcalculator.preference;

import android.app.Dialog;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import pl.srw.billcalculator.R;

/**
 * Created by Kamil Seweryn.
 */
public class PgnigSettingsFragment extends PricesSettingsFragment {

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setWspKonwersjiDescription();
    }

    @Override
    protected int getPreferencesResource() {
        return R.xml.pgnig_preferences;
    }

    @Override
    protected int getHelpLayoutResource() {
        return R.layout.pgnig_settings_help;
    }

    private void setWspKonwersjiDescription() {
        EditTextPreference wspKonwersjiPreference = (EditTextPreference) findPreference(getString(R.string.preferences_pgnig_wsp_konwersji));
        wspKonwersjiPreference.setDialogMessage(Html.fromHtml(getString(R.string.wsp_konwersji_desc)));
        wspKonwersjiPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Dialog dialog = ((EditTextPreference) preference).getDialog();
                TextView tvDesc = (TextView) dialog.findViewById(android.R.id.message);
                tvDesc.setMovementMethod(LinkMovementMethod.getInstance());
                return false;
            }
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


}
