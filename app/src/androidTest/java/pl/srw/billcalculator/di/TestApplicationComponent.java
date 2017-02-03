package pl.srw.billcalculator.di;

import javax.inject.Singleton;

import dagger.Component;
import pl.srw.billcalculator.form.view.InstantAutoCompleteTextInputEditTextAndroidTest;

@Singleton
@Component(modules = ApplicationModule.class)
public interface TestApplicationComponent extends ApplicationComponent {

    void inject(InstantAutoCompleteTextInputEditTextAndroidTest test);
}
