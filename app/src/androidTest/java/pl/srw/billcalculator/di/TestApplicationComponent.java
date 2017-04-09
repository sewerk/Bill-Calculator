package pl.srw.billcalculator.di;

import javax.inject.Singleton;

import dagger.Component;
import pl.srw.billcalculator.form.view.InstantAutoCompleteUITest;
import pl.srw.billcalculator.history.HistoryUITest;
import pl.srw.billcalculator.settings.SettingsUITest;

@Singleton
@Component(modules = ApplicationModule.class)
public interface TestApplicationComponent extends ApplicationComponent {

    void inject(InstantAutoCompleteUITest test);

    void inject(SettingsUITest settingsUITest);

    void inject(HistoryUITest historyUITest);
}
