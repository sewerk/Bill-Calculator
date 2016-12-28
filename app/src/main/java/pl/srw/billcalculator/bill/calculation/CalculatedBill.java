package pl.srw.billcalculator.bill.calculation;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import pl.srw.billcalculator.util.Dates;

public abstract class CalculatedBill {

    private static final BigDecimal VAT = new BigDecimal("0.23");

    private final boolean greedy;

    private BigDecimal netChargeSum = BigDecimal.ZERO;
    private BigDecimal vatChargeSum = BigDecimal.ZERO;

    private final int monthCount;

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

    public int getMonthCount() {
        return monthCount;
    }

    public BigDecimal getNetChargeSum() {
        return netChargeSum;
    }

    public BigDecimal getVatChargeSum() {
        return round(vatChargeSum);
    }

    public BigDecimal getGrossChargeSum() {
        return getNetChargeSum().add(getVatChargeSum());
    }

    protected BigDecimal round(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }
}
