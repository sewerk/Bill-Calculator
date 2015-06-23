package pl.srw.billcalculator;

import android.app.Application;
import android.content.Context;

import hugo.weaving.DebugLog;
import pl.srw.billcalculator.persistence.Database;
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

        AnalyticsWrapper.initialize(this);
        Database.initialize(this);

        //PreferenceMigration.migrate(getApplicationContext());

        new PgePrices().init();
        new PgnigPrices().init();
        new TauronPrices().init();
    }

}
