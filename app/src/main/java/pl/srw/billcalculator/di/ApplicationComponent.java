package pl.srw.billcalculator.di;

import javax.inject.Singleton;

import dagger.Component;
import pl.srw.billcalculator.BillCalculator;
import pl.srw.billcalculator.history.di.HistoryComponent;
import pl.srw.billcalculator.settings.di.ProviderSettingsComponent;
import pl.srw.billcalculator.settings.di.SettingsComponent;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    HistoryComponent getHistoryComponent();

    SettingsComponent getSettingsComponent();

    ProviderSettingsComponent getProviderSettingsComponent();

    void inject(BillCalculator application);
}
