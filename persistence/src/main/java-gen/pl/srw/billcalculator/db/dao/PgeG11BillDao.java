package pl.srw.billcalculator.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.internal.SqlUtils;

import java.util.ArrayList;
import java.util.List;

import pl.srw.billcalculator.db.PgeG11Bill;
import pl.srw.billcalculator.db.PgePrices;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "PGE_G11_BILL".
*/
@SuppressWarnings("ALL")
public class PgeG11BillDao extends AbstractDao<PgeG11Bill, Long> {

    public static final String TABLENAME = "PGE_G11_BILL";

    /**
     * Properties of entity PgeG11Bill.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property ReadingFrom = new Property(1, Integer.class, "readingFrom", false, "READING_FROM");
        public final static Property ReadingTo = new Property(2, Integer.class, "readingTo", false, "READING_TO");
        public final static Property DateFrom = new Property(3, java.util.Date.class, "dateFrom", false, "DATE_FROM");
        public final static Property DateTo = new Property(4, java.util.Date.class, "dateTo", false, "DATE_TO");
        public final static Property AmountToPay = new Property(5, Double.class, "amountToPay", false, "AMOUNT_TO_PAY");
        public final static Property PricesId = new Property(6, Long.class, "pricesId", false, "PRICES_ID");
    }

    private DaoSession daoSession;


    public PgeG11BillDao(DaoConfig config) {
        super(config);
    }
    
    public PgeG11BillDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"PGE_G11_BILL\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"READING_FROM\" INTEGER," + // 1: readingFrom
                "\"READING_TO\" INTEGER," + // 2: readingTo
                "\"DATE_FROM\" INTEGER," + // 3: dateFrom
                "\"DATE_TO\" INTEGER," + // 4: dateTo
                "\"AMOUNT_TO_PAY\" REAL," + // 5: amountToPay
                "\"PRICES_ID\" INTEGER);"); // 6: pricesId
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"PGE_G11_BILL\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, PgeG11Bill entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer readingFrom = entity.getReadingFrom();
        if (readingFrom != null) {
            stmt.bindLong(2, readingFrom);
        }
 
        Integer readingTo = entity.getReadingTo();
        if (readingTo != null) {
            stmt.bindLong(3, readingTo);
        }
 
        java.util.Date dateFrom = entity.getDateFrom();
        if (dateFrom != null) {
            stmt.bindLong(4, dateFrom.getTime());
        }
 
        java.util.Date dateTo = entity.getDateTo();
        if (dateTo != null) {
            stmt.bindLong(5, dateTo.getTime());
        }
 
        Double amountToPay = entity.getAmountToPay();
        if (amountToPay != null) {
            stmt.bindDouble(6, amountToPay);
        }
 
        Long pricesId = entity.getPricesId();
        if (pricesId != null) {
            stmt.bindLong(7, pricesId);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, PgeG11Bill entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer readingFrom = entity.getReadingFrom();
        if (readingFrom != null) {
            stmt.bindLong(2, readingFrom);
        }
 
        Integer readingTo = entity.getReadingTo();
        if (readingTo != null) {
            stmt.bindLong(3, readingTo);
        }
 
        java.util.Date dateFrom = entity.getDateFrom();
        if (dateFrom != null) {
            stmt.bindLong(4, dateFrom.getTime());
        }
 
        java.util.Date dateTo = entity.getDateTo();
        if (dateTo != null) {
            stmt.bindLong(5, dateTo.getTime());
        }
 
        Double amountToPay = entity.getAmountToPay();
        if (amountToPay != null) {
            stmt.bindDouble(6, amountToPay);
        }
 
        Long pricesId = entity.getPricesId();
        if (pricesId != null) {
            stmt.bindLong(7, pricesId);
        }
    }

    @Override
    protected final void attachEntity(PgeG11Bill entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public PgeG11Bill readEntity(Cursor cursor, int offset) {
        PgeG11Bill entity = new PgeG11Bill( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // readingFrom
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // readingTo
            cursor.isNull(offset + 3) ? null : new java.util.Date(cursor.getLong(offset + 3)), // dateFrom
            cursor.isNull(offset + 4) ? null : new java.util.Date(cursor.getLong(offset + 4)), // dateTo
            cursor.isNull(offset + 5) ? null : cursor.getDouble(offset + 5), // amountToPay
            cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6) // pricesId
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, PgeG11Bill entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setReadingFrom(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setReadingTo(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setDateFrom(cursor.isNull(offset + 3) ? null : new java.util.Date(cursor.getLong(offset + 3)));
        entity.setDateTo(cursor.isNull(offset + 4) ? null : new java.util.Date(cursor.getLong(offset + 4)));
        entity.setAmountToPay(cursor.isNull(offset + 5) ? null : cursor.getDouble(offset + 5));
        entity.setPricesId(cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(PgeG11Bill entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(PgeG11Bill entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(PgeG11Bill entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getPgePricesDao().getAllColumns());
            builder.append(" FROM PGE_G11_BILL T");
            builder.append(" LEFT JOIN PGE_PRICES T0 ON T.\"PRICES_ID\"=T0.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected PgeG11Bill loadCurrentDeep(Cursor cursor, boolean lock) {
        PgeG11Bill entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        PgePrices pgePrices = loadCurrentOther(daoSession.getPgePricesDao(), cursor, offset);
        entity.setPgePrices(pgePrices);

        return entity;    
    }

    public PgeG11Bill loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<PgeG11Bill> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<PgeG11Bill> list = new ArrayList<PgeG11Bill>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<PgeG11Bill> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<PgeG11Bill> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
