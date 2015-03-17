package pl.srw.billcalculator.persistence;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Kamil Seweryn.
 */
public class DBMigration {

    public static void migrate(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        Log.i("DBMigration", "Upgrading schema from version " + oldVersion + " to " + newVersion);
        switch (oldVersion) {
            case 1: migrate_1_2(db);
            //case 2: migrate_2_3(db);
        }
    }

    private static void migrate_1_2(final SQLiteDatabase db) {
        //TODO
    }
}
