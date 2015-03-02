package pl.srw.billcalculator.test;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Date;

import pl.srw.billcalculator.MainActivity;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.preference.PgeSettingsFragment;

/**
 * Created by Kamil Seweryn.
 */
public class PreferenceUtil {

    public static void markAfterFirstLaunch(final Context context) {
        context.getSharedPreferences(MainActivity.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE)
                .edit().putString(MainActivity.PREFERENCE_KEY_FIRST_LAUNCH, new Date().toString())
                .commit();
    }

    public static void clearFirstLaunch(final Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(MainActivity.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE).edit();
        editor.clear().commit();
    }

    public static String getFirstLaunchValue(final Context context) {
        return context.getSharedPreferences(MainActivity.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE)
                .getString(MainActivity.PREFERENCE_KEY_FIRST_LAUNCH, "");
    }

    public static void changeToG11Tariff(final Context context) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit().putString(context.getString(R.string.preferences_pge_tariff), PgeSettingsFragment.TARIFF_G11)
                .commit();
    }

    public static void changeToG12Tariff(final Context context) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit().putString(context.getString(R.string.preferences_pge_tariff), PgeSettingsFragment.TARIFF_G12)
                .commit();
    }
}
