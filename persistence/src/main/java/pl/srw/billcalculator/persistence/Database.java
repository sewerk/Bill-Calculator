package pl.srw.billcalculator.persistence;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import pl.srw.billcalculator.DaoMaster;
import pl.srw.billcalculator.DaoSession;
import pl.srw.billcalculator.PgeBillDao;

/**
 * Created by Kamil Seweryn.
 */
public class Database {

    public static final String QUERY_ROW_LIMIT = "100";
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

    public static List<Integer> queryCurrentReadings(CurrentReadingType readingType) {
        String[] columns = {readingType.getColumnName()};
        String where = "" + columns[0] + " is not null";
        Cursor cursor = database.query(true, readingType.getTableName(), columns, where, null, null, null, columns[0], QUERY_ROW_LIMIT);

        List<Integer> readings = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()) {
            readings.add(cursor.getInt(0));
        }
        return readings;
    }
    
}
