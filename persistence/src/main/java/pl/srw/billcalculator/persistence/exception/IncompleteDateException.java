package pl.srw.billcalculator.persistence.exception;

/**
 * Created by Kamil Seweryn.
 */
public class IncompleteDateException extends RuntimeException {

    public IncompleteDateException(final String dbTableName) {
        super("Storing entry " + dbTableName + " with incomplete data.");
    }
}
