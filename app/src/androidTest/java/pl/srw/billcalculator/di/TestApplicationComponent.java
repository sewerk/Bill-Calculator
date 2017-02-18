package pl.srw.billcalculator.di;

import javax.inject.Singleton;

import dagger.Component;
import pl.srw.billcalculator.bill.activity.AbstractVerifyBillCreationUITest;
import pl.srw.billcalculator.form.view.InstantAutoCompleteTextInputEditTextAndroidTest;
import pl.srw.billcalculator.settings.SettingsAndroidTest;

@Singleton
@Component(modules = ApplicationModule.class)
public interface TestApplicationComponent extends ApplicationComponent {

    void inject(InstantAutoCompleteTextInputEditTextAndroidTest test);

    void inject(SettingsAndroidTest settingsAndroidTest);

    void inject(AbstractVerifyBillCreationUITest test);
}
