package pl.srw.billcalculator.bill.calculation;

import java.math.BigDecimal;

/**
 * Created by kseweryn on 09.06.15.
 */
public abstract class TauronCalculatedBill extends CalculatedEnergyBill {

    protected TauronCalculatedBill(String dateFrom, String dateTo, String oplataAbonamentowaPrice, String oplataPrzejsciowaPrice, String oplataStalaZaPrzesylPrice) {
        super(dateFrom, dateTo, oplataAbonamentowaPrice, oplataPrzejsciowaPrice, oplataStalaZaPrzesylPrice);
    }

    public abstract BigDecimal getSellNetCharge();

    public abstract BigDecimal getSellVatCharge();

    public final BigDecimal getSellGrossCharge() {
        return getSellNetCharge().add(getSellVatCharge());
    }

    public BigDecimal getDistributeNetCharge() {
        return getNetChargeSum().subtract(getSellNetCharge());
    }

    public BigDecimal getDistributeVatCharge() {
        return getVatChargeSum().subtract(getSellVatCharge());
    }

    public final BigDecimal getDistributeGrossCharge() {
        return getDistributeNetCharge().add(getDistributeVatCharge());
    }
}
