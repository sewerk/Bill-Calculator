package pl.srw.billcalculator.persistence;

import org.greenrobot.greendao.Property;

/**
* Created by Kamil Seweryn.
*/
@SuppressWarnings("SameParameterValue")
public class SqlBuilder {

    private final StringBuilder sql;

    private SqlBuilder(final StringBuilder sql) {
        this.sql = sql;
    }

    public static SqlBuilder createTrigger(final String triggerName) {
        final StringBuilder sql = new StringBuilder("CREATE TRIGGER " + triggerName);
        return new SqlBuilder(sql);
    }

    public SqlBuilder after(final String afterOp) {
        sql.append(" AFTER ").append(afterOp);
        return this;
    }

    public SqlBuilder on(final String onTable) {
        sql.append(" ON ").append(onTable);
        return this;
    }

    public String execute(final String innerSql) {
        return sql.append(" BEGIN ").append(innerSql).append(" END;").toString();
    }

    public static SqlBuilder insertInto(final String tablename) {
        final StringBuilder sql = new StringBuilder("INSERT INTO " + tablename);
        return new SqlBuilder(sql);
    }

    public SqlBuilder cols(final Property... cols) {
        sql.append("(");
        for (Property col : cols) {
            sql.append(col.columnName);
            sql.append(",");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(")");
        return this;
    }

    public String vals(final String... vals) {
        sql.append(" VALUES(");
        for (String val : vals) {
            sql.append(val);
            sql.append(",");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(");");
        return sql.toString();
    }

    public static SqlBuilder deleteFrom(final String tablename) {
        final StringBuilder sql = new StringBuilder("DELETE FROM " + tablename);
        return new SqlBuilder(sql);
    }

    public SqlBuilder whereEq(final Property col, final String val) {
        sql.append(" WHERE ").append(col.columnName).append(" = ").append(val);
        return this;
    }

    public String andEq(final Property col, final String val) {
        sql.append(" AND ").append(col.columnName).append(" = ").append(val).append(";");
        return sql.toString();
    }

    public static String getNew(final Property col) {
        return "new." + col.columnName;
    }

    public static String getOld(final Property col) {
        return "old." + col.columnName;
    }
}
