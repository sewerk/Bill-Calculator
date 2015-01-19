package pl.srw.billcalculator.persistence;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import pl.srw.billcalculator.db.dao.DaoMaster;
import pl.srw.billcalculator.db.dao.DaoSession;
import pl.srw.billcalculator.db.dao.PgeBillDao;
import pl.srw.billcalculator.db.dao.PgeG12BillDao;
import pl.srw.billcalculator.db.dao.PgnigBillDao;

/**
 * Created by Kamil Seweryn.
 */
public class Database {

    public static final String QUERY_ROW_LIMIT = "100";
    private static PgeBillDao pgeBillDao;
    private static SQLiteDatabase database;
    private static PgeG12BillDao pgeG12BillDao;
    private static PgnigBillDao pgnigBillDao;

    public static void initialize(Context context) {
        final DaoSession daoSession = new DaoMaster(getDatabase(context)).newSession();

        pgeBillDao = daoSession.getPgeBillDao();
        pgeG12BillDao = daoSession.getPgeG12BillDao();
        pgnigBillDao = daoSession.getPgnigBillDao();
    }

    private static synchronized SQLiteDatabase getDatabase(Context context) {
        if (database == null)
            database = new DaoMaster.DevOpenHelper(context, "pl.srw.billcalculator.db", null)
                .getWritableDatabase();
        return database;
    }

    public static PgeBillDao getPgeBillDao() {
        return pgeBillDao;
    }

    public static PgeG12BillDao getPgeG12BillDao() {
        return pgeG12BillDao;
    }

    public static PgnigBillDao getPgnigBillDao() {
        return pgnigBillDao;
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
