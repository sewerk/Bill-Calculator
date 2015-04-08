package pl.srw.billcalculator;

import android.app.Application;
import android.content.Context;

import de.greenrobot.event.EventBus;
import pl.srw.billcalculator.persistence.Database;
import pl.srw.billcalculator.settings.GeneralPreferences;
import pl.srw.billcalculator.settings.prices.PgePrices;
import pl.srw.billcalculator.settings.prices.PgnigPrices;

/**
 * Created by Kamil Seweryn.
 */
public class BillCalculator extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        if (BuildConfig.DEBUG) {
            Database.enableDatabaseLogging();
        }
        Database.initialize(this);

        //PreferenceMigration.migrate(getApplicationContext());

        if (GeneralPreferences.isFirstLaunch()) {
            new PgePrices().setDefault();
            new PgnigPrices().setDefault();
        }
    }
    
}
