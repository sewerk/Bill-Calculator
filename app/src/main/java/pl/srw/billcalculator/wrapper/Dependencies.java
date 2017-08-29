package pl.srw.billcalculator.wrapper;

import android.content.Context;

import pl.srw.billcalculator.BillCalculator;
import pl.srw.billcalculator.bill.service.PgeBillStoringService;
import pl.srw.billcalculator.bill.service.PgnigBillStoringService;
import pl.srw.billcalculator.bill.service.TauronBillStoringService;
import pl.srw.billcalculator.di.ApplicationComponent;
import pl.srw.billcalculator.di.ApplicationModule;
import pl.srw.billcalculator.di.DaggerApplicationComponent;

public class Dependencies {

    private static ApplicationComponent applicationComponent;

    public static void init(Context application) {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(application))
                .build();
    }

    public static ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    public static void inject(BillCalculator application) {
        applicationComponent.inject(application);
    }

    public static void inject(PgeBillStoringService service) {
        applicationComponent.inject(service);
    }

    public static void inject(PgnigBillStoringService service) {
        applicationComponent.inject(service);
    }

    public static void inject(TauronBillStoringService service) {
        applicationComponent.inject(service);
    }
}
