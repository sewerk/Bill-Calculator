package pl.srw.billcalculator.di;

import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Component;
import pl.srw.billcalculator.BillCalculator;
import pl.srw.billcalculator.bill.di.PgeBillComponent;
import pl.srw.billcalculator.bill.di.PgnigBillComponent;
import pl.srw.billcalculator.bill.di.TauronBillComponent;
import pl.srw.billcalculator.bill.service.PgeBillStoringService;
import pl.srw.billcalculator.bill.service.PgnigBillStoringService;
import pl.srw.billcalculator.bill.service.TauronBillStoringService;
import pl.srw.billcalculator.history.di.HistoryComponent;
import pl.srw.billcalculator.settings.di.ProviderSettingsComponent;
import pl.srw.billcalculator.settings.di.SettingsComponent;
import pl.srw.billcalculator.settings.global.SettingsRepo;
import pl.srw.billcalculator.settings.prices.PgePrices;
import pl.srw.billcalculator.util.BillSelection;
import pl.srw.billcalculator.wrapper.PricesRepo;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    @NonNull HistoryComponent getHistoryComponent();

    @NonNull SettingsComponent getSettingsComponent();

    @NonNull ProviderSettingsComponent getProviderSettingsComponent();

    @NonNull PgeBillComponent getPgeBillComponent();

    @NonNull PgnigBillComponent getPgnigBillComponent();

    @NonNull TauronBillComponent getTauronBillComponent();

    void inject(BillCalculator application);

    void inject(PgeBillStoringService service);

    void inject(PgnigBillStoringService service);

    void inject(TauronBillStoringService service);

    // Visible for UI tests
    @NonNull PgePrices getPgePrices();

    @NonNull SettingsRepo getSettingsRepo();

    @NonNull BillSelection getBillSelection();

    @NonNull PricesRepo getPricesRepo();
}
