package pl.srw.billcalculator.db.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import de.greenrobot.dao.AbstractDaoMaster;
import de.greenrobot.dao.identityscope.IdentityScopeType;

import pl.srw.billcalculator.db.dao.PgePricesDao;
import pl.srw.billcalculator.db.dao.PgeG11BillDao;
import pl.srw.billcalculator.db.dao.PgeG12BillDao;
import pl.srw.billcalculator.db.dao.PgnigPricesDao;
import pl.srw.billcalculator.db.dao.PgnigBillDao;
import pl.srw.billcalculator.db.dao.HistoryDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * Master of DAO (schema version 1): knows all DAOs.
*/
public class DaoMaster extends AbstractDaoMaster {
    public static final int SCHEMA_VERSION = 1;

    /** Creates underlying database table using DAOs. */
    public static void createAllTables(SQLiteDatabase db, boolean ifNotExists) {
        PgePricesDao.createTable(db, ifNotExists);
        PgeG11BillDao.createTable(db, ifNotExists);
        PgeG12BillDao.createTable(db, ifNotExists);
        PgnigPricesDao.createTable(db, ifNotExists);
        PgnigBillDao.createTable(db, ifNotExists);
        HistoryDao.createTable(db, ifNotExists);
    }
    
    /** Drops underlying database table using DAOs. */
    public static void dropAllTables(SQLiteDatabase db, boolean ifExists) {
        PgePricesDao.dropTable(db, ifExists);
        PgeG11BillDao.dropTable(db, ifExists);
        PgeG12BillDao.dropTable(db, ifExists);
        PgnigPricesDao.dropTable(db, ifExists);
        PgnigBillDao.dropTable(db, ifExists);
        HistoryDao.dropTable(db, ifExists);
    }
    
    public static abstract class OpenHelper extends SQLiteOpenHelper {

        public OpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory, SCHEMA_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i("greenDAO", "Creating tables for schema version " + SCHEMA_VERSION);
            createAllTables(db, false);
        }
    }
    
    /** WARNING: Drops all table on Upgrade! Use only during development. */
    public static class DevOpenHelper extends OpenHelper {
        public DevOpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            dropAllTables(db, true);
            onCreate(db);
        }
    }

    public DaoMaster(SQLiteDatabase db) {
        super(db, SCHEMA_VERSION);
        registerDaoClass(PgePricesDao.class);
        registerDaoClass(PgeG11BillDao.class);
        registerDaoClass(PgeG12BillDao.class);
        registerDaoClass(PgnigPricesDao.class);
        registerDaoClass(PgnigBillDao.class);
        registerDaoClass(HistoryDao.class);
    }
    
    public DaoSession newSession() {
        return new DaoSession(db, IdentityScopeType.Session, daoConfigMap);
    }
    
    public DaoSession newSession(IdentityScopeType type) {
        return new DaoSession(db, type, daoConfigMap);
    }
    
}
