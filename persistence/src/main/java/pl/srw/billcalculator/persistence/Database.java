package pl.srw.billcalculator.persistence;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.LazyList;
import de.greenrobot.dao.query.QueryBuilder;
import pl.srw.billcalculator.db.History;
import pl.srw.billcalculator.db.dao.DaoMaster;
import pl.srw.billcalculator.db.dao.DaoSession;
import pl.srw.billcalculator.db.dao.HistoryDao;
import pl.srw.billcalculator.persistence.type.BillType;
import pl.srw.billcalculator.persistence.type.CurrentReadingType;

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
            database = new DaoMaster.DevOpenHelper(context, "pl.srw.billcalculator.db", null) {

                @Override
                public void onCreate(final SQLiteDatabase db) {
                    super.onCreate(db);
                    Triggers.create(db);
                }

                @Override
                public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
                    DBMigration.migrate(db, oldVersion, newVersion);
                }
            }.getWritableDatabase();
        return database;
    }

    public static DaoSession getSession() {
        return daoSession;
    }

    public static List<Integer> queryCurrentReadings(CurrentReadingType readingType) {
        String[] columns = {readingType.getColumnName()};
        Cursor cursor = database.query(true, readingType.getTableName(), columns, null, null, null, null, columns[0] + " DESC", QUERY_ROW_LIMIT);

        List<Integer> readings = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()) {
            readings.add(cursor.getInt(0));
        }
        return readings;
    }

    public static LazyList<History> getHistory() {
        return getSession().getHistoryDao().queryBuilder()
                .orderDesc(HistoryDao.Properties.DateFrom, HistoryDao.Properties.Id).listLazy();
    }

    public static void deleteBillWithPrices(final BillType type, final Long billId, final Long pricesId) {
        switch (type) {
            case PGE_G11:
                getSession().getPgePricesDao().deleteByKey(pricesId);
                getSession().getPgeG11BillDao().deleteByKey(billId);
                return;
            case PGE_G12:
                getSession().getPgePricesDao().deleteByKey(pricesId);
                getSession().getPgeG12BillDao().deleteByKey(billId);
                return;
            case PGNIG:
                getSession().getPgnigPricesDao().deleteByKey(pricesId);
                getSession().getPgnigBillDao().deleteByKey(billId);
                return;
        }
    }
}