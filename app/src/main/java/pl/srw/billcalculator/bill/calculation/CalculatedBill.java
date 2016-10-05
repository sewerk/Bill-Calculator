package pl.srw.billcalculator.bill.calculation;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import lombok.Getter;
import pl.srw.billcalculator.util.Dates;

/**
 * Calculates and represent each bill position value.
 * <br/>
 * Net value precision is not modified.<br/>
 * Vat value precision is Net.precision+2
 */
public abstract class CalculatedBill {

    private static final BigDecimal VAT = new BigDecimal("0.23");

    private final boolean greedy;

    @Getter private BigDecimal netChargeSum = BigDecimal.ZERO;
    private BigDecimal vatChargeSum = BigDecimal.ZERO;

    @Getter private final int monthCount;

    protected CalculatedBill(final boolean greedy, final String dateFrom, final String dateTo) {
        this.greedy = greedy;
        monthCount = Dates.countWholeMonth(dateFrom, dateTo);
    }

    protected BigDecimal countNetAndAddToSum(final String price, final int count) {
        return countNetAndAddToSum(price, new BigDecimal(count));
    }

    protected BigDecimal countNetAndAddToSum(final String price, final BigInteger count) {
        return countNetAndAddToSum(price, new BigDecimal(count));
    }

    protected BigDecimal countNetAndAddToSum(String price, BigDecimal count) {
        BigDecimal netCharge = new BigDecimal(price).multiply(count);
        netChargeSum = netChargeSum.add(round(netCharge));
        return netCharge;
    }

    protected BigDecimal countVatAndAddToSum(final BigDecimal netCharge) {
        BigDecimal vatCharge = netCharge.multiply(VAT);
        if (greedy)
            vatChargeSum = vatChargeSum.add(round(vatCharge));
        else
            vatChargeSum = vatChargeSum.add(vatCharge);
        return vatCharge;
    }

    public BigDecimal getVatChargeSum() {
        return round(vatChargeSum);
    }

    public BigDecimal getGrossChargeSum() {
        return getNetChargeSum().add(getVatChargeSum());
    }

    /**
     * Round value for scale=2
     */
    protected BigDecimal round(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }
}
