package pl.srw.billcalculator.persistence;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.LazyList;
import de.greenrobot.dao.query.QueryBuilder;
import pl.srw.billcalculator.db.Bill;
import pl.srw.billcalculator.db.PgeBill;
import pl.srw.billcalculator.db.dao.DaoMaster;
import pl.srw.billcalculator.db.dao.DaoSession;
import pl.srw.billcalculator.db.dao.PgeBillDao;

/**
 * Created by Kamil Seweryn.
 */
public class Database {

    public static final String QUERY_ROW_LIMIT = "100";
    private static SQLiteDatabase database;
    private static DaoSession daoSession;

    public static void initialize(Context context) {
        daoSession = new DaoMaster(getDatabase(context)).newSession();
    }

    public static void enableDatabaseLogging() {
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
    }

    private static synchronized SQLiteDatabase getDatabase(Context context) {
        if (database == null)
            database = new DaoMaster.DevOpenHelper(context, "pl.srw.billcalculator.db", null)
                .getWritableDatabase();
        return database;
    }

    public static DaoSession getSession() {
        return daoSession;
    }

    public static List<Integer> queryCurrentReadings(CurrentReadingType readingType) {
        String[] columns = {readingType.getColumnName()};
//        String where = "" + columns[0] + " is not null";
        Cursor cursor = database.query(true, readingType.getTableName(), columns, null, null, null, null, columns[0], QUERY_ROW_LIMIT);

        List<Integer> readings = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()) {
            readings.add(cursor.getInt(0));
        }
        return readings;
    }

}
