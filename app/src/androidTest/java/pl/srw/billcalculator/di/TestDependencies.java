package pl.srw.billcalculator.di;

import pl.srw.billcalculator.form.view.InstantAutoCompleteUITest;
import pl.srw.billcalculator.history.HistoryUITest;
import pl.srw.billcalculator.settings.SettingsUITest;
import pl.srw.billcalculator.wrapper.Dependencies;

/**
 * Setup test component to access production instances
 */
public class TestDependencies {

    private static TestApplicationComponent component = DaggerTestApplicationComponent.builder()
            .testApplicationModule(new TestApplicationModule(Dependencies.getApplicationComponent()))
            .build();

    public static void inject(HistoryUITest test) {
        component.inject(test);
    }

    public static void inject(InstantAutoCompleteUITest test) {
        component.inject(test);
    }

    public static void inject(SettingsUITest test) {
        component.inject(test);
    }
}
