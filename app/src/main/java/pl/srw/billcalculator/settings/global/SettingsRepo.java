package pl.srw.billcalculator.settings.global;

import android.content.SharedPreferences;

import java.util.Date;

import javax.inject.Singleton;

@Singleton
public class SettingsRepo {

    private static final String PREFERENCE_KEY_FIRST_LAUNCH = "first_launch";
    private SharedPreferences prefs;

    public SettingsRepo(SharedPreferences prefs) {
        this.prefs = prefs;
    }

    public boolean isFirstLaunch() {
        return prefs.getString(PREFERENCE_KEY_FIRST_LAUNCH, "").isEmpty();
    }

    public void markFirstLaunch() {
        prefs.edit().putString(PREFERENCE_KEY_FIRST_LAUNCH, new Date().toString()).apply();
    }
}
