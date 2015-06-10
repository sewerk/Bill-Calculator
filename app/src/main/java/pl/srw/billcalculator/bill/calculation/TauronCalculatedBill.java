package pl.srw.billcalculator.bill.calculation;

import java.math.BigDecimal;
import java.math.RoundingMode;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * Created by kseweryn on 09.06.15.
 */
@SuppressWarnings("FieldCanBeLocal")
@Getter(value = AccessLevel.PROTECTED)
public abstract class TauronCalculatedBill extends CalculatedEnergyBill {

    private final BigDecimal absDistributeNetCharge;
    private final BigDecimal absDistributeVatCharge;

    protected TauronCalculatedBill(String dateFrom, String dateTo, String oplataAbonamentowaPrice, String oplataPrzejsciowaPrice, String oplataStalaZaPrzesylPrice) {
        super(dateFrom, dateTo, oplataAbonamentowaPrice, oplataPrzejsciowaPrice, oplataStalaZaPrzesylPrice);

        absDistributeNetCharge = sum(getOplataDystrybucyjnaStalaNetCharge(),
                getOplataPrzejsciowaNetCharge(), getOplataAbonamentowaNetCharge());
        absDistributeVatCharge = sum(getOplataDystrybucyjnaStalaVatCharge(),
                getOplataPrzejsciowaVatCharge(), getOplataAbonamentowaVatCharge());
    }

    protected BigDecimal sum(BigDecimal... values) {
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal val : values)
            sum = sum.add(val.setScale(2, RoundingMode.HALF_UP));
        return sum;
    }

    public abstract BigDecimal getSellNetCharge();

    public abstract BigDecimal getSellVatCharge();

    public final BigDecimal getSellGrossCharge() {
        return sum(getSellNetCharge(), getSellVatCharge());
    }

    public abstract BigDecimal getDistributeNetCharge();

    public abstract BigDecimal getDistributeVatCharge();

    public final BigDecimal getDistributeGrossCharge() {
        return sum(getDistributeNetCharge(), getDistributeVatCharge());
    }
}
