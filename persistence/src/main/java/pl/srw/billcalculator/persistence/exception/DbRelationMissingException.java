package pl.srw.billcalculator.persistence.exception;

/**
 * Created by Kamil Seweryn.
 */
public class DbRelationMissingException extends RuntimeException {

    public DbRelationMissingException(final String table1, final String table2) {
        super("Inconsistent database relation: " + table1 + " <-> " + table2);
    }
}
