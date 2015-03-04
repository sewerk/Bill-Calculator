package pl.srw.billcalculator.calculation;

import java.math.BigDecimal;
import java.math.RoundingMode;

import lombok.Getter;
import pl.srw.billcalculator.util.Dates;

/**
 * Created by Kamil Seweryn.
 */
@Getter
public abstract class CalculatedBill {

    public static final BigDecimal VAT = new BigDecimal("0.23");

    private BigDecimal netChargeSum = BigDecimal.ZERO;
    private BigDecimal vatChargeSum = BigDecimal.ZERO;

    private final int monthCount;

    protected CalculatedBill(final String dateFrom, final String dateTo) {
        monthCount = Dates.countMonth(dateFrom, dateTo);
    }

    protected BigDecimal multiplyAndAddToSum(final String oplataAbonamentowa, final int count) {
        BigDecimal netCharge = new BigDecimal(oplataAbonamentowa).multiply(new BigDecimal(count));
        netChargeSum = netChargeSum.add(netCharge.setScale(2, RoundingMode.HALF_UP));
        return netCharge;
    }

    protected BigDecimal multiplyVatAndAddToSum(final BigDecimal netCharge) {
        BigDecimal vatCharge = netCharge.multiply(VAT);
        vatChargeSum = vatChargeSum.add(vatCharge.setScale(2, RoundingMode.HALF_UP));
        return vatCharge;
    }

    public BigDecimal getGrossChargeSum() {
        return netChargeSum.add(vatChargeSum);
    }
}
