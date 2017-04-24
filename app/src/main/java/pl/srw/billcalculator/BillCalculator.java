package pl.srw.billcalculator;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;

import com.jakewharton.threetenabp.AndroidThreeTen;
import com.squareup.leakcanary.LeakCanary;

import javax.inject.Inject;

import hugo.weaving.DebugLog;
import pl.srw.billcalculator.persistence.Database;
import pl.srw.billcalculator.settings.prices.PgePrices;
import pl.srw.billcalculator.settings.prices.PgnigPrices;
import pl.srw.billcalculator.settings.prices.TauronPrices;
import pl.srw.billcalculator.wrapper.Analytics;
import pl.srw.billcalculator.wrapper.Dependencies;
import timber.log.Timber;

public class BillCalculator extends Application {

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

        Dependencies.init(getApplicationContext());
        Dependencies.inject(this);

        Analytics.initialize(this);
        AndroidThreeTen.init(this);
        Database.initialize(this);

        pgePrices.setDefaultIfNotSet();
        pgnigPrices.setDefaultIfNotSet();
        tauronPrices.setDefaultIfNotSet();

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public static BillCalculator get(Context context) {
        return (BillCalculator) context.getApplicationContext();
    }
}
