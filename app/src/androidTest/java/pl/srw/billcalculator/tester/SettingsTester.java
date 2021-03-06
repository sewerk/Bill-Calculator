package pl.srw.billcalculator.tester;

import android.support.test.espresso.ViewInteraction;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.type.Provider;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

public class SettingsTester extends Tester {

    private final AppTester parent;
    private final ProviderSettingsTester<SettingsTester> providerSettingsTester;

    public SettingsTester() {
        this(new AppTester());
    }

    SettingsTester(AppTester parent) {
        this.parent = parent;
        this.providerSettingsTester = new ProviderSettingsTester<>(this);
    }

    public ProviderSettingsTester<SettingsTester> pickProvider(Provider provider) {
        ViewInteraction settingsItem = onView(
                allOf(withId(R.id.settings_list_item),
                        childAtPosition(withId(R.id.settingsList), provider.ordinal())));
        settingsItem.perform(click());
        return providerSettingsTester;
    }

    public AppTester close() {
        if (!isOnTablet()) {
            pressBack();
        }
        return parent;
    }
}
