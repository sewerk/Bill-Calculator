package pl.srw.billcalculator.persistence.exception;

/**
 * Created by Kamil Seweryn.
 */
public class DbNotInitializedYetException extends RuntimeException {

    public DbNotInitializedYetException() {
        super("Calling method before Database finish initializing");
    }
}
