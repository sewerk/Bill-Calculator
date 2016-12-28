package pl.srw.billcalculator.wrapper;

import android.content.Context;

import pl.srw.billcalculator.BillCalculator;
import pl.srw.billcalculator.bill.service.PgeBillStoringService;
import pl.srw.billcalculator.bill.service.PgnigBillStoringService;
import pl.srw.billcalculator.bill.service.TauronBillStoringService;
import pl.srw.billcalculator.di.ApplicationComponent;

public class Dependencies {

    public static void inject(PgeBillStoringService service) {
        getApplicationComponent(service).inject(service);
    }

    public static void inject(PgnigBillStoringService service) {
        getApplicationComponent(service).inject(service);
    }

    public static void inject(TauronBillStoringService service) {
        getApplicationComponent(service).inject(service);
    }

    private static ApplicationComponent getApplicationComponent(Context context) {
        return BillCalculator.get(context).getApplicationComponent();
    }
}
