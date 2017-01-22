package pl.srw.billcalculator;

import android.app.Application;
import android.content.Context;

import com.jakewharton.threetenabp.AndroidThreeTen;
import com.squareup.leakcanary.LeakCanary;

import javax.inject.Inject;

import hugo.weaving.DebugLog;
import pl.srw.billcalculator.di.ApplicationComponent;
import pl.srw.billcalculator.di.ApplicationModule;
import pl.srw.billcalculator.di.DaggerApplicationComponent;
import pl.srw.billcalculator.persistence.Database;
import pl.srw.billcalculator.settings.prices.PgePrices;
import pl.srw.billcalculator.settings.prices.PgnigPrices;
import pl.srw.billcalculator.settings.prices.TauronPrices;
import timber.log.Timber;

public class BillCalculator extends Application {

    private ApplicationComponent applicationComponent;

    @Inject PgePrices pgePrices;
    @Inject PgnigPrices pgnigPrices;
    @Inject TauronPrices tauronPrices;

    @DebugLog
    @Override
    public void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            Database.enableDatabaseLogging();
        }

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(getApplicationContext()))
                .build();
        applicationComponent.inject(this);
//        Analytics.initialize(this);
        AndroidThreeTen.init(this);
        Database.initialize(this);

        //PreferenceMigration.migrate(getApplicationContext());

        pgePrices.setDefaultIfNotSet();
        pgnigPrices.setDefaultIfNotSet();
        tauronPrices.setDefaultIfNotSet();
    }

    public static BillCalculator get(Context context) {
        return (BillCalculator) context.getApplicationContext();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
