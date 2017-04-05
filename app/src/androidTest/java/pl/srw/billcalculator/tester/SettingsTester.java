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

    private AppTester parent;
    private ProviderSettingsTester<SettingsTester> providerSettingsTester = new ProviderSettingsTester<>(this);

    SettingsTester(AppTester parent) {
        this.parent = parent;
    }

    public ProviderSettingsTester<SettingsTester> pickProvider(Provider provider) {
        ViewInteraction settingsItem = onView(
                allOf(withId(R.id.settings_list_item),
                        childAtPosition(withId(R.id.list), provider.ordinal())));
        settingsItem.perform(click());
        return providerSettingsTester;
    }

    public AppTester close() {
        pressBack();
        return parent;
    }
}
