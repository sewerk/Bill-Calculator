package pl.srw.billcalculator;

/**
 * Created by kseweryn on 17.06.15.
 */
public class CrashlyticsWrapper {

    public static void setInt(String key, int val) {
    }

    public static void setString(String key, String val) {
    }

    private static boolean isEnabled() {
        return !BuildConfig.DEBUG && !BuildConfig.DEVEL;
    }
}
