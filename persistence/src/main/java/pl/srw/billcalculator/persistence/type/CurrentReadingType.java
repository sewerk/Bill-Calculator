package pl.srw.billcalculator.persistence.type;

import pl.srw.billcalculator.db.dao.PgeG11BillDao;
import pl.srw.billcalculator.db.dao.PgeG12BillDao;
import pl.srw.billcalculator.db.dao.PgnigBillDao;

/**
 * Created by Kamil Seweryn.
 */
public enum CurrentReadingType {

    PGNIG_TO(PgnigBillDao.TABLENAME, PgnigBillDao.Properties.ReadingTo.columnName),
    PGE_TO(PgeG11BillDao.TABLENAME, PgeG11BillDao.Properties.ReadingTo.columnName),
    PGE_DAY_TO(PgeG12BillDao.TABLENAME, PgeG12BillDao.Properties.ReadingDayTo.columnName),
    PGE_NIGHT_TO(PgeG12BillDao.TABLENAME, PgeG12BillDao.Properties.ReadingNightTo.columnName);


    private final String tableName;
    private final String columnName;

    CurrentReadingType(String tableName, String columnName) {
        this.tableName = tableName;
        this.columnName = columnName;
    }

    public String getTableName() {
        return tableName;
    }

    public String getColumnName() {
        return columnName;
    }
}
