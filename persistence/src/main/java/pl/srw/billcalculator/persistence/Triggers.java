package pl.srw.billcalculator.persistence;

import android.database.sqlite.SQLiteDatabase;

import pl.srw.billcalculator.db.dao.HistoryDao;
import pl.srw.billcalculator.db.dao.PgeG11BillDao;
import pl.srw.billcalculator.db.dao.PgeG12BillDao;
import pl.srw.billcalculator.db.dao.PgnigBillDao;
import pl.srw.billcalculator.db.dao.TauronG11BillDao;
import pl.srw.billcalculator.db.dao.TauronG12BillDao;
import pl.srw.billcalculator.persistence.type.BillType;

import static pl.srw.billcalculator.persistence.SqlTriggerBuilder.*;

/**
 * Created by Kamil Seweryn.
 */
public final class Triggers {

    private static final String DELETE = "DELETE";
    private static final String INSERT = "INSERT";

    private static final String[] BILL_INSERT_TRIGGERS = {
            insertHistoryTrigger("bill_pgeG11_insert_trigger", PgeG11BillDao.TABLENAME, BillType.PGE_G11.toString()),
            insertHistoryTrigger("bill_pgeG12_insert_trigger", PgeG12BillDao.TABLENAME, BillType.PGE_G12.toString()),
            insertHistoryTrigger("bill_pgnig_insert_trigger", PgnigBillDao.TABLENAME, BillType.PGNIG.toString()),
            insertHistoryTrigger("bill_tauronG11_insert_trigger", TauronG11BillDao.TABLENAME, BillType.TAURON_G11.toString()),
            insertHistoryTrigger("bill_tauronG12_insert_trigger", TauronG12BillDao.TABLENAME, BillType.TAURON_G12.toString())
    };
    private static final String[] BILL_DELETE_TRIGGERS = {
            deleteHistoryTrigger("bill_pgeG11_delete_trigger", PgeG11BillDao.TABLENAME, BillType.PGE_G11.toString()),
            deleteHistoryTrigger("bill_pgeG12_delete_trigger", PgeG12BillDao.TABLENAME, BillType.PGE_G12.toString()),
            deleteHistoryTrigger("bill_pgnig_delete_trigger", PgnigBillDao.TABLENAME, BillType.PGNIG.toString()),
            deleteHistoryTrigger("bill_tauronG11_delete_trigger", TauronG11BillDao.TABLENAME, BillType.TAURON_G11.toString()),
            deleteHistoryTrigger("bill_tauronG12_delete_trigger", TauronG12BillDao.TABLENAME, BillType.TAURON_G12.toString())
    };
    private static final int DB_VER_2_FROM_TRIGGER = 3;
    private static final int DB_VER_2_TO_TRIGGER = 4;

    public static void create(SQLiteDatabase db) {
        for (String sql : Triggers.BILL_INSERT_TRIGGERS)
            db.execSQL(sql);
        for (String sql : Triggers.BILL_DELETE_TRIGGERS)
            db.execSQL(sql);
    }

    public static void update(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1: addTriggers(db, DB_VER_2_FROM_TRIGGER, DB_VER_2_TO_TRIGGER);
        }
    }

    private static void addTriggers(SQLiteDatabase db, int fromIdx, int toIdx) {
        for (int i = fromIdx; i <= toIdx; i++) {
            db.execSQL(Triggers.BILL_INSERT_TRIGGERS[i]);
            db.execSQL(Triggers.BILL_DELETE_TRIGGERS[i]);
        }
    }

    private static String insertHistoryTrigger(final String triggerName, final String tableName, final String billType) {
        return createTrigger(triggerName)
                .after(INSERT)
                .on(tableName)
                .execute(insertInto(HistoryDao.TABLENAME)
                        .cols(HistoryDao.Properties.DateFrom, HistoryDao.Properties.BillType, HistoryDao.Properties.BillId)
                        .vals(getNew(PgeG11BillDao.Properties.DateFrom), "'" + billType + "'", getNew(PgeG11BillDao.Properties.Id)));
    }

    private static String deleteHistoryTrigger(final String triggerName, final String tableName, final String billType) {
        return createTrigger(triggerName)
                .after(DELETE)
                .on(tableName)
                .execute(deleteFrom(HistoryDao.TABLENAME)
                        .whereEq(HistoryDao.Properties.BillId, getOld(PgeG11BillDao.Properties.Id))
                        .andEq(HistoryDao.Properties.BillType, "'" + billType + "'"));
    }
}
