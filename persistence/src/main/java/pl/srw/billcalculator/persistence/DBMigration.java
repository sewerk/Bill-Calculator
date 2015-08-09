package pl.srw.billcalculator.persistence;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import pl.srw.billcalculator.db.dao.DaoMaster;
import pl.srw.billcalculator.db.dao.TauronG11BillDao;
import pl.srw.billcalculator.db.dao.TauronG12BillDao;
import pl.srw.billcalculator.db.dao.TauronPricesDao;

/**
 * Created by Kamil Seweryn.
 */
class DBMigration {

    public static final int CURRENT_VERSION = DaoMaster.SCHEMA_VERSION;

    public static void migrate(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        Log.i("DBMigration", "Upgrading schema from version " + oldVersion + " to " + newVersion);
        switch (oldVersion) {
            case 1: migrate_1_2(db);
            //case 2: migrate_2_3(db);
        }
    }

    private static void migrate_1_2(final SQLiteDatabase db) {
        TauronPricesDao.createTable(db, true);
        TauronG11BillDao.createTable(db, true);
        TauronG12BillDao.createTable(db, true);
    }
}
