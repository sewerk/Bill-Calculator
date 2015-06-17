package pl.srw.billcalculator;

import android.content.Context;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

/**
 * Created by kseweryn on 17.06.15.
 */
public class CrashlyticsWrapper {

    public static void initialize(Context context) {
        if (isEnabled())
            Fabric.with(context, new Crashlytics());
    }

    public static void setInt(String key, int val) {
        if (isEnabled())
            Crashlytics.setInt(key, val);
    }

    public static void setString(String key, String val) {
        if (isEnabled())
            Crashlytics.setString(key, val);
    }

    private static boolean isEnabled() {
        return !BuildConfig.DEBUG && !BuildConfig.DEVEL;
    }
}
