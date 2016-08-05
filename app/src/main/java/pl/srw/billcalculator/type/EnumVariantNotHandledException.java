package pl.srw.billcalculator.type;

public class EnumVariantNotHandledException extends RuntimeException {

    public EnumVariantNotHandledException(Enum<?> value) {
        super("Enum value not handled: " + value);
    }
}
