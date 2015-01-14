package pl.srw.billcalculator.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import pl.srw.billcalculator.DaoMaster;
import pl.srw.billcalculator.DaoSession;
import pl.srw.billcalculator.PgeBillDao;

/**
 * Created by Kamil Seweryn.
 */
public class Database {

    private static PgeBillDao pgeBillDao;
    private static SQLiteDatabase database;

    public static void initialize(Context context) {
        final DaoSession daoSession = new DaoMaster(getDatabase(context)).newSession();

        pgeBillDao = daoSession.getPgeBillDao();
    }

    private static synchronized SQLiteDatabase getDatabase(Context context) {
        if (database == null)
            database = new DaoMaster.DevOpenHelper(context, "pl.srw.billcalculator", null)
                .getWritableDatabase();
        return database;
    }

    public static PgeBillDao getPgeBillDao() {
        return pgeBillDao;
    }
    
}
