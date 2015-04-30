package pl.srw.billcalculator.type;

/**
 * Created by kseweryn on 30.04.15.
 */
public class EnumVariantNotHandledException extends RuntimeException {

    public EnumVariantNotHandledException(Enum<?> value) {
        super("Enum value not handled: " + value);
    }
}
