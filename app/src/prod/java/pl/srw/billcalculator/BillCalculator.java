package pl.srw.billcalculator;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;

import hugo.weaving.DebugLog;
import io.fabric.sdk.android.Fabric;
import pl.srw.billcalculator.persistence.Database;
import pl.srw.billcalculator.settings.GeneralPreferences;
import pl.srw.billcalculator.settings.prices.PgePrices;
import pl.srw.billcalculator.settings.prices.PgnigPrices;
import pl.srw.billcalculator.settings.prices.TauronPrices;

/**
 * Created by Kamil Seweryn.
 */
public class BillCalculator extends Application {

    public static Context context;

    @DebugLog
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        if (!BuildConfig.DEBUG)
            Fabric.with(this, new Crashlytics());

        Database.initialize(this);

        //PreferenceMigration.migrate(getApplicationContext());

        if (GeneralPreferences.isFirstLaunch()) {
            new PgePrices().setDefault();
            new PgnigPrices().setDefault();
            new TauronPrices().setDefault();
        }
    }

}
