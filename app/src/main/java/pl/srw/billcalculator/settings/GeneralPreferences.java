package pl.srw.billcalculator.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.VisibleForTesting;

import java.util.Date;

import pl.srw.billcalculator.BillCalculator;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.settings.fragment.PgeSettingsFragment;

/**
 * Created by Kamil Seweryn.
 */
public final class GeneralPreferences {

    @VisibleForTesting
    public static final String SHARED_PREFERENCES_FILE = "PreferencesFile";
    @VisibleForTesting
    public static final String PREFERENCE_KEY_FIRST_LAUNCH = "first_launch";

    private static final Context context = BillCalculator.context;

    private GeneralPreferences() {}

    public static boolean isPgeTariffG12() {
        //noinspection ConstantConditions
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.preferences_pge_tariff), "").equals(PgeSettingsFragment.TARIFF_G12);
    }

    public static boolean isTauronTariffG12() {
        //noinspection ConstantConditions
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString("preferences_tauron_tariff", "").equals(PgeSettingsFragment.TARIFF_G12);
    }

    public static boolean isFirstLaunch() {
        //noinspection ConstantConditions
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
