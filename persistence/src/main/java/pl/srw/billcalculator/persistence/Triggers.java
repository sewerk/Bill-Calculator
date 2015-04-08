package pl.srw.billcalculator.persistence;

import android.database.sqlite.SQLiteDatabase;

import pl.srw.billcalculator.db.dao.HistoryDao;
import pl.srw.billcalculator.db.dao.PgeG11BillDao;
import pl.srw.billcalculator.db.dao.PgeG12BillDao;
import pl.srw.billcalculator.db.dao.PgnigBillDao;
import pl.srw.billcalculator.persistence.type.BillType;

import static pl.srw.billcalculator.persistence.SqlBuilder.*;

/**
 * Created by Kamil Seweryn.
 */
public final class Triggers {

    private static final String DELETE = "DELETE";
    private static final String INSERT = "INSERT";

    private static final String[] BILL_INSERT_TRIGGERS = {
            insertHistoryTrigger("bill_pgeG11_insert_trigger", PgeG11BillDao.TABLENAME, BillType.PGE_G11.toString()),
            insertHistoryTrigger("bill_pgeG12_insert_trigger", PgeG12BillDao.TABLENAME, BillType.PGE_G12.toString()),
            insertHistoryTrigger("bill_pgnig_insert_trigger", PgnigBillDao.TABLENAME, BillType.PGNIG.toString())
    };
    private static final String[] BILL_DELETE_TRIGGERS = {
            deleteHistoryTrigger("bill_pgeG11_delete_trigger", PgeG11BillDao.TABLENAME, BillType.PGE_G11.toString()),
            deleteHistoryTrigger("bill_pgeG12_delete_trigger", PgeG12BillDao.TABLENAME, BillType.PGE_G12.toString()),
            deleteHistoryTrigger("bill_pgnig_delete_trigger", PgnigBillDao.TABLENAME, BillType.PGNIG.toString())
    };

    public static void create(SQLiteDatabase db) {
        for (String sql : Triggers.BILL_INSERT_TRIGGERS)
            db.execSQL(sql);
        for (String sql : Triggers.BILL_DELETE_TRIGGERS)
            db.execSQL(sql);

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
