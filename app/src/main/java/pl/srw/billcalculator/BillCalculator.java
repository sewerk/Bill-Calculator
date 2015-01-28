package pl.srw.billcalculator;

import android.app.Application;
import android.content.Context;

import pl.srw.billcalculator.persistence.Database;

/**
 * Created by Kamil Seweryn.
 */
public class BillCalculator extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        
        Database.initialize(this);
        if (BuildConfig.DEBUG) {
            Database.enableDatabaseLogging();
        }

        //PreferenceMigration.migrate(getApplicationContext());
    }
    
}
