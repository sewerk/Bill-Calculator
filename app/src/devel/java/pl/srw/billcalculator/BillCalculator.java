package pl.srw.billcalculator;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;

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

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        Database.enableDatabaseLogging();
        LeakCanary.install(this);

        Database.initialize(this);

        //PreferenceMigration.migrate(getApplicationContext());

        if (GeneralPreferences.isFirstLaunch()) {
            new PgePrices().setDefault();
            new PgnigPrices().setDefault();
            new TauronPrices().setDefault();
        }
    }

}
