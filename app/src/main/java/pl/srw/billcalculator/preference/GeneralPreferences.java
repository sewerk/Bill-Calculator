package pl.srw.billcalculator.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Date;

import pl.srw.billcalculator.BillCalculator;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.preference.fragment.PgeSettingsFragment;

/**
 * Created by Kamil Seweryn.
 */
public final class GeneralPreferences {

    public static final String SHARED_PREFERENCES_FILE = "PreferencesFile";
    public static final String PREFERENCE_KEY_FIRST_LAUNCH = "first_launch";

    private static Context context = BillCalculator.context;

    private GeneralPreferences() {}

    public static boolean isPgeTariffG12() {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.preferences_pge_tariff), "").equals(PgeSettingsFragment.TARIFF_G12);
    }

    public static boolean isFirstLaunch() {
        return getSharedPreferences()
                .getString(PREFERENCE_KEY_FIRST_LAUNCH, "").isEmpty();
    }

    public static void markFirstLaunch() {
        getSharedPreferences()
                .edit().putString(PREFERENCE_KEY_FIRST_LAUNCH, new Date().toString()).apply();
    }

    private static SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
    }
}
