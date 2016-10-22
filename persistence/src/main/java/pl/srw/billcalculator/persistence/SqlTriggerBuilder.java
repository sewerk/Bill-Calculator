package pl.srw.billcalculator.persistence;

import de.greenrobot.dao.Property;

public class SqlTriggerBuilder {

    private final StringBuilder sql;

    private SqlTriggerBuilder(final StringBuilder sql) {
        this.sql = sql;
    }

    public static SqlTriggerBuilder createTrigger(final String triggerName) {
        final StringBuilder sql = new StringBuilder("CREATE TRIGGER " + triggerName);
        return new SqlTriggerBuilder(sql);
    }

    public SqlTriggerBuilder after(final String afterOp) {
        sql.append(" AFTER ").append(afterOp);
        return this;
    }

    public SqlTriggerBuilder on(final String onTable) {
        sql.append(" ON ").append(onTable);
        return this;
    }

    public String execute(final String innerSql) {
        return sql.append(" BEGIN ").append(innerSql).append(" END;").toString();
    }

    public static SqlTriggerBuilder insertInto(final String tablename) {
        final StringBuilder sql = new StringBuilder("INSERT INTO " + tablename);
        return new SqlTriggerBuilder(sql);
    }

    public SqlTriggerBuilder cols(final Property... cols) {
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

    public static SqlTriggerBuilder deleteFrom(final String tablename) {
        final StringBuilder sql = new StringBuilder("DELETE FROM " + tablename);
        return new SqlTriggerBuilder(sql);
    }

    public SqlTriggerBuilder whereEq(final Property col, final String val) {
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
