package pl.srw.billcalculator;

import android.app.Application;

import pl.srw.billcalculator.persistence.Database;

/**
 * Created by Kamil Seweryn.
 */
public class BillCalculator extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        
        Database.initialize(this);
        if (BuildConfig.DEBUG) {
            Database.enableDatabaseLogging();
        }

        //PreferenceMigration.migrate(getApplicationContext());
    }
    
}
