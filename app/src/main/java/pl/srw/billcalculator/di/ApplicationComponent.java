package pl.srw.billcalculator.di;

import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Component;
import pl.srw.billcalculator.BillCalculator;
import pl.srw.billcalculator.bill.di.PgeBillComponent;
import pl.srw.billcalculator.bill.di.PgnigBillComponent;
import pl.srw.billcalculator.bill.di.TauronBillComponent;
import pl.srw.billcalculator.data.ApplicationRepo;
import pl.srw.billcalculator.data.settings.prices.PricesRepo;
import pl.srw.billcalculator.history.di.HistoryComponent;
import pl.srw.billcalculator.settings.di.SettingsComponent;
import pl.srw.billcalculator.settings.prices.PgePrices;
import pl.srw.billcalculator.util.BillSelection;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    @NonNull HistoryComponent getHistoryComponent();

    @NonNull SettingsComponent getSettingsComponent();

    @NonNull PgeBillComponent getPgeBillComponent();

    @NonNull PgnigBillComponent getPgnigBillComponent();

    @NonNull TauronBillComponent getTauronBillComponent();

    void inject(BillCalculator application);

    // Visible for UI tests
    @NonNull PgePrices getPgePrices();

    @NonNull ApplicationRepo getApplicationRepo();

    @NonNull BillSelection getBillSelection();

    @NonNull PricesRepo getPricesRepo();

    @NonNull Context getContext();
}
