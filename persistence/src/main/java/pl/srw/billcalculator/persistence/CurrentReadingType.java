package pl.srw.billcalculator.persistence;

import pl.srw.billcalculator.PgeBillDao;

/**
 * Created by Kamil Seweryn.
 */
public enum CurrentReadingType {

    PGNIG_TO(PgeBillDao.TABLENAME, PgeBillDao.Properties.ReadingTo.columnName),//TODO to be implemented
    PGE_TO(PgeBillDao.TABLENAME, PgeBillDao.Properties.ReadingTo.columnName),
    PGE_DAY_TO(PgeBillDao.TABLENAME, PgeBillDao.Properties.ReadingDayTo.columnName),
    PGE_NIGHT_TO(PgeBillDao.TABLENAME, PgeBillDao.Properties.ReadingNightTo.columnName);


    private final String tableName;
    private final String columnName;

    private CurrentReadingType(String tableName, String columnName) {
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
