package pl.srw.billcalculator.tester;

import android.support.annotation.IntDef;
import android.support.test.espresso.ViewInteraction;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import pl.srw.billcalculator.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

public class SettingsTester extends Tester {

    public static final int PGE = 0;
    public static final int PGNIG = PGE + 1;
    public static final int TAURON = PGNIG + 1;

    @IntDef({PGE, PGNIG, TAURON})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Provider {}

    private AppTester parent;
    private ProviderSettingsTester<SettingsTester> providerSettingsTester = new ProviderSettingsTester<>(this);

    public SettingsTester(AppTester parent) {
        this.parent = parent;
    }

    public ProviderSettingsTester<SettingsTester> pickProvider(@Provider int provider) {
        ViewInteraction settingsItem = onView(
                allOf(withId(R.id.settings_list_item),
                        childAtPosition(withId(R.id.list), provider)));
        settingsItem.perform(click());
        return providerSettingsTester;
    }

    public AppTester close() {
        pressBack();
        return parent;
    }
}
