package pl.srw.billcalculator.persistence;

import android.database.sqlite.SQLiteDatabase;

import pl.srw.billcalculator.db.History;
import pl.srw.billcalculator.db.dao.HistoryDao;
import pl.srw.billcalculator.db.dao.PgeG11BillDao;
import pl.srw.billcalculator.db.dao.PgeG12BillDao;
import pl.srw.billcalculator.db.dao.PgnigBillDao;
import pl.srw.billcalculator.persistence.type.BillType;

/**
 * Created by Kamil Seweryn.
 */
public final class Triggers {

    public static final String[] BILL_INSERT_TRIGGERS = {
            insertTrigger("bill_pgeG11_insert_trigger", PgeG11BillDao.TABLENAME, BillType.PGE_G11.toString()),
            insertTrigger("bill_pgeG12_insert_trigger", PgeG12BillDao.TABLENAME, BillType.PGE_G12.toString()),
            insertTrigger("bill_pgnig_insert_trigger", PgnigBillDao.TABLENAME, BillType.PGNIG.toString())
    };
    private static String[] BILL_DELETE_TRIGGERS = {
            deleteTrigger("bill_pgeG11_delete_trigger", PgeG11BillDao.TABLENAME, BillType.PGE_G11.toString()),
            deleteTrigger("bill_pgeG12_delete_trigger", PgeG12BillDao.TABLENAME, BillType.PGE_G12.toString()),
            deleteTrigger("bill_pgnig_delete_trigger", PgnigBillDao.TABLENAME, BillType.PGNIG.toString())
    };

    private static String insertTrigger(final String triggerName, final String tableName, final String billType) {
        return "CREATE TRIGGER " + triggerName + " AFTER INSERT ON " + tableName + " BEGIN " +
                getInnerInsert(billType) + " END;";
    }

    private static String getInnerInsert(final String billType) {
        return "INSERT INTO " + HistoryDao.TABLENAME +
                "(" +
                HistoryDao.Properties.DateFrom.columnName + "," + HistoryDao.Properties.BillType.columnName + "," + HistoryDao.Properties.BillId.columnName +
                ") VALUES(" +
                "new." + PgeG11BillDao.Properties.DateFrom.columnName + ", '" + billType + "', new." + PgeG11BillDao.Properties.Id.columnName +
                ");";
    }

    private static String deleteTrigger(final String triggerName, final String tableName, final String billType) {
        return "CREATE TRIGGER " + triggerName + " AFTER DELETE ON " + tableName + " BEGIN " +
                getInnerDelete(billType) + " END;";
    }

    private static String getInnerDelete(String billType) {
        return "DELETE FROM " + HistoryDao.TABLENAME + 
                " WHERE " + HistoryDao.Properties.BillId.columnName + " = old." + PgeG11BillDao.Properties.Id.columnName +
                " AND " + HistoryDao.Properties.BillType.columnName + " = '" + billType + "';";
    }

    public static void create(SQLiteDatabase db) {
        for (String sql : Triggers.BILL_INSERT_TRIGGERS)
            db.execSQL(sql);
        for (String sql : Triggers.BILL_DELETE_TRIGGERS)
            db.execSQL(sql);
        
    }
}
