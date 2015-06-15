package pl.srw.billcalculator;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;

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

        Database.enableDatabaseLogging();
        LeakCanary.install(this);
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());

        Database.initialize(this);

        //PreferenceMigration.migrate(getApplicationContext());

        new PgePrices().init();
        new PgnigPrices().init();
        new TauronPrices().init();
    }

}
