package pl.srw.billcalculator.settings.global;

import android.content.SharedPreferences;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import static pl.srw.billcalculator.di.ApplicationModule.GLOBAL_SHARED_PREFS;

@Singleton
public class SettingsRepo {

    private static final String PREFERENCE_KEY_FIRST_LAUNCH = "first_launch";
    private SharedPreferences prefs;

    @Inject
    SettingsRepo(@Named(GLOBAL_SHARED_PREFS) SharedPreferences prefs) {
        this.prefs = prefs;
    }

    public boolean isFirstLaunch() {
        return prefs.getString(PREFERENCE_KEY_FIRST_LAUNCH, "").isEmpty();
    }

    public void markFirstLaunch() {
        prefs.edit().putString(PREFERENCE_KEY_FIRST_LAUNCH, new Date().toString()).apply();
    }
}
