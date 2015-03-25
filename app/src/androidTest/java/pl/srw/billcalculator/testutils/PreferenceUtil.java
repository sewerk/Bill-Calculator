package pl.srw.billcalculator.testutils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.preference.GeneralPreferences;
import pl.srw.billcalculator.preference.fragment.PgeSettingsFragment;

/**
 * Created by Kamil Seweryn.
 */
public class PreferenceUtil {

    public static void clearFirstLaunch(final Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(GeneralPreferences.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE).edit();
        editor.remove(GeneralPreferences.PREFERENCE_KEY_FIRST_LAUNCH).apply();
    }

    public static String getFirstLaunchValue(final Context context) {
        return context.getSharedPreferences(GeneralPreferences.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE)
                .getString(GeneralPreferences.PREFERENCE_KEY_FIRST_LAUNCH, "");
    }

    public static void changeToG11Tariff(final Context context) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit().putString(context.getString(R.string.preferences_pge_tariff), PgeSettingsFragment.TARIFF_G11)
                .apply();
    }

    public static void changeToG12Tariff(final Context context) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit().putString(context.getString(R.string.preferences_pge_tariff), PgeSettingsFragment.TARIFF_G12)
                .apply();
    }
}
