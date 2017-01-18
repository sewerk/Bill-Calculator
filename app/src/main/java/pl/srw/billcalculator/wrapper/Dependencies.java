package pl.srw.billcalculator.wrapper;

import android.content.Context;

import com.f2prateek.dart.Dart;

import butterknife.ButterKnife;
import pl.srw.billcalculator.AboutActivity;
import pl.srw.billcalculator.BillCalculator;
import pl.srw.billcalculator.bill.activity.PgeBillActivity;
import pl.srw.billcalculator.bill.activity.PgnigBillActivity;
import pl.srw.billcalculator.bill.activity.TauronBillActivity;
import pl.srw.billcalculator.bill.service.PgeBillStoringService;
import pl.srw.billcalculator.bill.service.PgnigBillStoringService;
import pl.srw.billcalculator.bill.service.TauronBillStoringService;
import pl.srw.billcalculator.di.ApplicationComponent;
import pl.srw.billcalculator.history.DrawerActivity;
import pl.srw.billcalculator.settings.activity.ProviderSettingsActivity;
import pl.srw.billcalculator.settings.activity.SettingsActivity;

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

    public static void inject(PgeBillActivity activity) {
        Dart.inject(activity);
        ButterKnife.bind(activity);
    }

    public static void inject(PgnigBillActivity activity) {
        Dart.inject(activity);
        ButterKnife.bind(activity);
    }

    public static void inject(TauronBillActivity activity) {
        Dart.inject(activity);
        ButterKnife.bind(activity);
    }

    private static ApplicationComponent getApplicationComponent(Context context) {
        return BillCalculator.get(context).getApplicationComponent();
    }

    public static void inject(SettingsActivity activity) {
        ButterKnife.bind(activity);
    }

    public static void inject(ProviderSettingsActivity activity) {
        Dart.inject(activity);
    }

    public static void inject(DrawerActivity activity) {
        ButterKnife.bind(activity);
    }

    public static void inject(AboutActivity activity) {
        ButterKnife.bind(activity);
    }
}