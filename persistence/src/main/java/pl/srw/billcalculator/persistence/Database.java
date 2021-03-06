package pl.srw.billcalculator.persistence;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import pl.srw.billcalculator.db.Bill;
import pl.srw.billcalculator.db.History;
import pl.srw.billcalculator.db.Prices;
import pl.srw.billcalculator.db.dao.DaoMaster;
import pl.srw.billcalculator.db.dao.DaoSession;
import pl.srw.billcalculator.db.dao.HistoryDao;
import pl.srw.billcalculator.persistence.type.BillType;
import pl.srw.billcalculator.persistence.type.CurrentReadingType;
import timber.log.Timber;

public class Database {

    public static final String DB_NAME = "pl.srw.billcalculator.db";
    private static final String QUERY_ROW_LIMIT = "100";
    private static DaoSession daoSession;
    private static Query<History> historyQuery;

    public static void initialize(Context context) {
        daoSession = new DaoMaster(getDatabase(context)).newSession();
    }

    public static void restart(Context context) {
        Timber.i("Database.restart()");
        close();
        initialize(context);
    }

    public static void enableDatabaseLogging() {
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
    }

    private static synchronized SQLiteDatabase getDatabase(Context context) {
        return new DaoMaster.OpenHelper(context, DB_NAME) {

            @Override
            public void onCreate(final SQLiteDatabase db) {
                super.onCreate(db);
                Triggers.create(db);
            }

            @Override
            public void onUpgrade(org.greenrobot.greendao.database.Database db, int oldVersion, int newVersion) {
                DBMigration.migrate(db, oldVersion, newVersion);
                Triggers.update(db, oldVersion, newVersion);
            }
        }.getWritableDatabase();
    }

    public static DaoSession getSession() {
        return daoSession;
    }

    public static int[] queryCurrentReadings(CurrentReadingType readingType) {
        String[] columns = {readingType.getColumnName()};
        final SQLiteDatabase database = (SQLiteDatabase) daoSession.getDatabase().getRawDatabase();
        Cursor cursor = database.query(true, readingType.getTableName(), columns, null, null, null, null, columns[0] + " DESC", QUERY_ROW_LIMIT);

        int[] readings = new int[cursor.getCount()];
        int idx = 0;
        while (cursor.moveToNext())
            readings[idx++] = cursor.getInt(0);
        cursor.close();
        return readings;
    }

    public static List<History> getHistory() {
        if (historyQuery == null)
            historyQuery = getSession().getHistoryDao().queryBuilder()
                    .orderDesc(HistoryDao.Properties.DateFrom, HistoryDao.Properties.BillType, HistoryDao.Properties.BillId)
                    .build();
        return historyQuery.list();
    }

    public static Bill loadBill(final BillType type, final Long billId) {
        return type.getDao().load(billId);
    }

    public static Prices loadPrices(final BillType type, final Long pricesId) {
        return type.getPricesDao().load(pricesId);
    }

    public static void deleteBillWithPrices(final BillType type, final Long billId, final Long pricesId) {
        type.getPricesDao().deleteByKey(pricesId);
        type.getDao().deleteByKey(billId);
    }

    public static <T> void insert(T entry) {
        daoSession.insert(entry);
    }

    private static void close() {
        if (daoSession != null) {
            daoSession.getDatabase().close();
            daoSession = null;
        }
    }
}
