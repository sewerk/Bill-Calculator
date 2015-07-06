package pl.srw.billcalculator.bill.calculation;

import java.math.BigDecimal;

import lombok.Getter;

/**
 * Created by Kamil Seweryn.
 */
@SuppressWarnings("FieldCanBeLocal")
@Getter
public abstract class CalculatedEnergyBill extends CalculatedBill {

    private static final BigDecimal EXCISE = new BigDecimal("0.02");

    private final BigDecimal oplataAbonamentowaNetCharge;
    private final BigDecimal oplataPrzejsciowaNetCharge;
    private final BigDecimal oplataDystrybucyjnaStalaNetCharge;

    private final BigDecimal oplataAbonamentowaVatCharge;
    private final BigDecimal oplataPrzejsciowaVatCharge;
    private final BigDecimal oplataDystrybucyjnaStalaVatCharge;

    protected CalculatedEnergyBill(final String dateFrom, final String dateTo, String oplataAbonamentowa, String oplataPrzejsciowa, String oplataStalaZaPrzesyl) {
        super(dateFrom, dateTo);

        oplataAbonamentowaNetCharge = countNetAndAddToSum(oplataAbonamentowa, getMonthCount());
        oplataPrzejsciowaNetCharge = countNetAndAddToSum(oplataPrzejsciowa, getMonthCount());
        oplataDystrybucyjnaStalaNetCharge = countNetAndAddToSum(oplataStalaZaPrzesyl, getMonthCount());

        oplataAbonamentowaVatCharge = countVatAndAddToSum(oplataAbonamentowaNetCharge);
        oplataPrzejsciowaVatCharge = countVatAndAddToSum(oplataPrzejsciowaNetCharge);
        oplataDystrybucyjnaStalaVatCharge = countVatAndAddToSum(oplataDystrybucyjnaStalaNetCharge);
    }

    public BigDecimal getExcise() {
        return EXCISE.multiply(new BigDecimal(getTotalConsumption()));
    }

    public abstract int getTotalConsumption();
}
