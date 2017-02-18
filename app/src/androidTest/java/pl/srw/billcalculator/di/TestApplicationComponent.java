package pl.srw.billcalculator.di;

import javax.inject.Singleton;

import dagger.Component;
import pl.srw.billcalculator.bill.activity.PgnigUITest;
import pl.srw.billcalculator.bill.activity.TauronUITest;
import pl.srw.billcalculator.settings.SettingsAndroidTest;
import pl.srw.billcalculator.form.view.InstantAutoCompleteTextInputEditTextAndroidTest;

@Singleton
@Component(modules = ApplicationModule.class)
public interface TestApplicationComponent extends ApplicationComponent {

    void inject(InstantAutoCompleteTextInputEditTextAndroidTest test);

    void inject(SettingsAndroidTest settingsAndroidTest);

    void inject(PgnigUITest pgnigUITest);

    void inject(TauronUITest tauronUITest);
}
