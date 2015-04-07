package pl.srw.billcalculator.history.list.provider;

import android.content.Intent;

import java.math.BigDecimal;

import hugo.weaving.DebugLog;
import pl.srw.billcalculator.BillCalculator;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.db.Bill;
import pl.srw.billcalculator.db.History;
import pl.srw.billcalculator.persistence.type.BillType;
import pl.srw.billcalculator.util.Dates;
import pl.srw.billcalculator.util.Display;

/**
 * Created by Kamil Seweryn.
 */
public abstract class HistoryItemValueProvider {

    protected HistoryItemValueProvider() { }

    @DebugLog
    public static HistoryItemValueProvider of(final History item) {
        switch (BillType.valueOf(item.getBillType())) {
            case PGE_G11:
                return new PgeG11BillHistoryItemValueProvider(item);
            case PGE_G12:
                return new PgeG12BillHistoryItemValueProvider(item);
            case PGNIG:
                return new PgnigBillHistoryItemValueProvider(item);
        }
        throw new RuntimeException("Bill type: " + item.getBillType() + " not handled.");
    }


    public abstract Bill getBill();

    public abstract int getLogoId();

    public abstract String getReadings();

    public String getDatePeriod() {
        return BillCalculator.context.getString(R.string.history_period,
                Dates.format(Dates.toLocalDate(getBill().getDateFrom())), 
                Dates.format(Dates.toLocalDate(getBill().getDateTo())));
    }

    public String getAmount() {
        final Double amountToPay = getBill().getAmountToPay();
        return getAmount(amountToPay);
    }

    public abstract Intent getIntent();

    protected String getAmount(final Double amountToPay) {
        final String amount = Display.toPay(new BigDecimal(amountToPay.toString()));
        return BillCalculator.context.getString(R.string.history_amount, amount);
    }

}