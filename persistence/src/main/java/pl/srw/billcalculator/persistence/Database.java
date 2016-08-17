package pl.srw.billcalculator.persistence;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.LazyList;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;
import pl.srw.billcalculator.db.Bill;
import pl.srw.billcalculator.db.History;
import pl.srw.billcalculator.db.Prices;
import pl.srw.billcalculator.db.dao.DaoMaster;
import pl.srw.billcalculator.db.dao.DaoSession;
import pl.srw.billcalculator.db.dao.HistoryDao;
import pl.srw.billcalculator.persistence.type.BillType;
import pl.srw.billcalculator.persistence.type.CurrentReadingType;

/**
 * Created by Kamil Seweryn.
 */
public class Database {

    private static final String DB_NAME = "pl.srw.billcalculator.db";
    private static final String QUERY_ROW_LIMIT = "100";
    private static SQLiteDatabase database;
    private static DaoSession daoSession;
    private static Query<History> historyQuery;
    private static Prices deletedPrices;
    private static Bill deletedBill;

    public static void initialize(Context context) {
        daoSession = new DaoMaster(getDatabase(context)).newSession();
    }

    public static void enableDatabaseLogging() {
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
    }

    private static synchronized SQLiteDatabase getDatabase(Context context) {
        if (database == null)
            database = new DaoMaster.DevOpenHelper(context, DB_NAME, null) {

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
        return database;
    }

    public static DaoSession getSession() {
        return daoSession;
    }

    public static int[] queryCurrentReadings(CurrentReadingType readingType) {
        String[] columns = {readingType.getColumnName()};
        Cursor cursor = database.query(true, readingType.getTableName(), columns, null, null, null, null, columns[0] + " DESC", QUERY_ROW_LIMIT);

        int[] readings = new int[cursor.getCount()];
        int idx = 0;
        while (cursor.moveToNext())
            readings[idx++] = cursor.getInt(0);
        cursor.close();
        return readings;
    }

    public static LazyList<History> getHistory() {
        if (historyQuery == null)
            historyQuery = getSession().getHistoryDao().queryBuilder()
                .orderDesc(HistoryDao.Properties.DateFrom, HistoryDao.Properties.BillType, HistoryDao.Properties.BillId).build();
        return historyQuery.listLazy();
    }

    public static void deleteBillWithPrices(final BillType type, final Long billId, final Long pricesId) {
        deletedPrices = type.getPricesDao().load(pricesId);
        type.getPricesDao().deleteByKey(pricesId);
        deletedBill = type.getDao().load(billId);
        type.getDao().deleteByKey(billId);
    }

    public static void undelete() {
        if (deletedBill != null && deletedPrices != null) {
            final BillType type = BillType.valueOf(deletedBill);
            ((AbstractDao<Prices, Long>) type.getPricesDao()).insert(deletedPrices);
            deletedPrices = null;
            ((AbstractDao<Bill, Long>) type.getDao()).insert(deletedBill);
            deletedBill = null;
        }
    }
}
