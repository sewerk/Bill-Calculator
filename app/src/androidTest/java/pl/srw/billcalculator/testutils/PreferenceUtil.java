package pl.srw.billcalculator.testutils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import pl.srw.billcalculator.BillCalculator;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.settings.GeneralPreferences;
import pl.srw.billcalculator.settings.fragment.PgeSettingsFragment;
import pl.srw.billcalculator.type.Provider;

/**
 * Created by Kamil Seweryn.
 */
public class PreferenceUtil {

    public static void clearFirstLaunch() {
        SharedPreferences.Editor editor = BillCalculator.context.getSharedPreferences(GeneralPreferences.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE).edit();
        editor.remove(GeneralPreferences.PREFERENCE_KEY_FIRST_LAUNCH).apply();
    }

    public static String getFirstLaunchValue() {
        return BillCalculator.context.getSharedPreferences(GeneralPreferences.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE)
                .getString(GeneralPreferences.PREFERENCE_KEY_FIRST_LAUNCH, "");
    }

    public static void changeToG11Tariff(Provider provider) {
        final String tariff = getTariffPrefKey(provider);
        PreferenceManager.getDefaultSharedPreferences(BillCalculator.context)
                .edit().putString(tariff, PgeSettingsFragment.TARIFF_G11)
                .apply();
    }

    public static void changeToG12Tariff(Provider provider) {
        final String tariff = getTariffPrefKey(provider);
        PreferenceManager.getDefaultSharedPreferences(BillCalculator.context)
                .edit().putString(tariff, PgeSettingsFragment.TARIFF_G12)
                .apply();
    }

    private static String getTariffPrefKey(Provider provider) {
        final String tariff;
        switch (provider) {
            case PGE:
                tariff = BillCalculator.context.getString(R.string.preferences_pge_tariff);
                break;
            case TAURON:
                tariff = "preferences_tauron_tariff";
                break;
            default:
                throw new RuntimeException("Unsupported " + provider);
        }
        return tariff;
    }
}
