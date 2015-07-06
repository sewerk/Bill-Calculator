package pl.srw.billcalculator.bill.calculation;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import lombok.Getter;
import pl.srw.billcalculator.util.Dates;

/**
 * Created by Kamil Seweryn.
 */
@Getter
public abstract class CalculatedBill {

    private static final BigDecimal VAT = new BigDecimal("0.23");

    private BigDecimal netChargeSum = BigDecimal.ZERO;
    private BigDecimal vatChargeSum = BigDecimal.ZERO;

    private final int monthCount;

    protected CalculatedBill(final String dateFrom, final String dateTo) {
        monthCount = Dates.countMonth(dateFrom, dateTo);
    }

    protected BigDecimal countNetAndAddToSum(final String price, final int count) {
        return countNetAndAddToSum(price, new BigDecimal(count));
    }

    protected BigDecimal countNetAndAddToSum(final String price, final BigInteger count) {
        return countNetAndAddToSum(price, new BigDecimal(count));
    }

    private BigDecimal countNetAndAddToSum(String price, BigDecimal count) {
        BigDecimal netCharge = new BigDecimal(price).multiply(count);
        netChargeSum = netChargeSum.add(netCharge.setScale(2, RoundingMode.HALF_UP));
        return netCharge;
    }

    protected BigDecimal countVatAndAddToSum(final BigDecimal netCharge) {
        BigDecimal vatCharge = netCharge.multiply(VAT);
        vatChargeSum = vatChargeSum.add(vatCharge.setScale(2, RoundingMode.HALF_UP));
        return vatCharge;
    }

    public BigDecimal getGrossChargeSum() {
        return netChargeSum.add(vatChargeSum);
    }
}
