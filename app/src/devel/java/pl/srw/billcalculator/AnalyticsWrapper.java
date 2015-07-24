package pl.srw.billcalculator;

import android.util.Log;

/**
 * Created by kseweryn on 17.06.15.
 */
public class AnalyticsWrapper {

    public static final String TAG = "AnalyticsLogger";

    public static void setInt(String key, int val) {
    }

    public static void setString(String key, String val) {
    }

    public static void log(String message) {
        Log.d(TAG, message);
    }

    private static boolean isEnabled() {
        return !BuildConfig.DEBUG;
    }
}
