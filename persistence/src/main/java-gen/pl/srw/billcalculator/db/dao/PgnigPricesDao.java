package pl.srw.billcalculator.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

import pl.srw.billcalculator.db.PgnigPrices;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "PGNIG_PRICES".
*/
@SuppressWarnings("ALL")
public class PgnigPricesDao extends AbstractDao<PgnigPrices, Long> {

    public static final String TABLENAME = "PGNIG_PRICES";

    /**
     * Properties of entity PgnigPrices.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property OplataAbonamentowa = new Property(1, String.class, "oplataAbonamentowa", false, "OPLATA_ABONAMENTOWA");
        public final static Property PaliwoGazowe = new Property(2, String.class, "paliwoGazowe", false, "PALIWO_GAZOWE");
        public final static Property DystrybucyjnaStala = new Property(3, String.class, "dystrybucyjnaStala", false, "DYSTRYBUCYJNA_STALA");
        public final static Property DystrybucyjnaZmienna = new Property(4, String.class, "dystrybucyjnaZmienna", false, "DYSTRYBUCYJNA_ZMIENNA");
        public final static Property WspolczynnikKonwersji = new Property(5, String.class, "wspolczynnikKonwersji", false, "WSPOLCZYNNIK_KONWERSJI");
        public final static Property OplataHandlowa = new Property(6, String.class, "oplataHandlowa", false, "OPLATA_HANDLOWA");
    }


    public PgnigPricesDao(DaoConfig config) {
        super(config);
    }
    
    public PgnigPricesDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"PGNIG_PRICES\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"OPLATA_ABONAMENTOWA\" TEXT," + // 1: oplataAbonamentowa
                "\"PALIWO_GAZOWE\" TEXT," + // 2: paliwoGazowe
                "\"DYSTRYBUCYJNA_STALA\" TEXT," + // 3: dystrybucyjnaStala
                "\"DYSTRYBUCYJNA_ZMIENNA\" TEXT," + // 4: dystrybucyjnaZmienna
                "\"WSPOLCZYNNIK_KONWERSJI\" TEXT," + // 5: wspolczynnikKonwersji
                "\"OPLATA_HANDLOWA\" TEXT);"); // 6: oplataHandlowa
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"PGNIG_PRICES\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, PgnigPrices entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String oplataAbonamentowa = entity.getOplataAbonamentowa();
        if (oplataAbonamentowa != null) {
            stmt.bindString(2, oplataAbonamentowa);
        }
 
        String paliwoGazowe = entity.getPaliwoGazowe();
        if (paliwoGazowe != null) {
            stmt.bindString(3, paliwoGazowe);
        }
 
        String dystrybucyjnaStala = entity.getDystrybucyjnaStala();
        if (dystrybucyjnaStala != null) {
            stmt.bindString(4, dystrybucyjnaStala);
        }
 
        String dystrybucyjnaZmienna = entity.getDystrybucyjnaZmienna();
        if (dystrybucyjnaZmienna != null) {
            stmt.bindString(5, dystrybucyjnaZmienna);
        }
 
        String wspolczynnikKonwersji = entity.getWspolczynnikKonwersji();
        if (wspolczynnikKonwersji != null) {
            stmt.bindString(6, wspolczynnikKonwersji);
        }
 
        String oplataHandlowa = entity.getOplataHandlowa();
        if (oplataHandlowa != null) {
            stmt.bindString(7, oplataHandlowa);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, PgnigPrices entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String oplataAbonamentowa = entity.getOplataAbonamentowa();
        if (oplataAbonamentowa != null) {
            stmt.bindString(2, oplataAbonamentowa);
        }
 
        String paliwoGazowe = entity.getPaliwoGazowe();
        if (paliwoGazowe != null) {
            stmt.bindString(3, paliwoGazowe);
        }
 
        String dystrybucyjnaStala = entity.getDystrybucyjnaStala();
        if (dystrybucyjnaStala != null) {
            stmt.bindString(4, dystrybucyjnaStala);
        }
 
        String dystrybucyjnaZmienna = entity.getDystrybucyjnaZmienna();
        if (dystrybucyjnaZmienna != null) {
            stmt.bindString(5, dystrybucyjnaZmienna);
        }
 
        String wspolczynnikKonwersji = entity.getWspolczynnikKonwersji();
        if (wspolczynnikKonwersji != null) {
            stmt.bindString(6, wspolczynnikKonwersji);
        }
 
        String oplataHandlowa = entity.getOplataHandlowa();
        if (oplataHandlowa != null) {
            stmt.bindString(7, oplataHandlowa);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public PgnigPrices readEntity(Cursor cursor, int offset) {
        PgnigPrices entity = new PgnigPrices( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // oplataAbonamentowa
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // paliwoGazowe
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // dystrybucyjnaStala
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // dystrybucyjnaZmienna
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // wspolczynnikKonwersji
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6) // oplataHandlowa
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, PgnigPrices entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setOplataAbonamentowa(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setPaliwoGazowe(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setDystrybucyjnaStala(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setDystrybucyjnaZmienna(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setWspolczynnikKonwersji(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setOplataHandlowa(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(PgnigPrices entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(PgnigPrices entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(PgnigPrices entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
