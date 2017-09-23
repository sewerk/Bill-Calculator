package pl.srw.billcalculator.history.list.provider;

import android.content.Context;

import java.math.BigDecimal;

import hugo.weaving.DebugLog;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.db.Bill;
import pl.srw.billcalculator.db.History;
import pl.srw.billcalculator.persistence.type.BillType;
import pl.srw.billcalculator.type.EnumVariantNotHandledException;
import pl.srw.billcalculator.util.Dates;
import pl.srw.billcalculator.util.Display;

public abstract class HistoryItemValueProvider {

    private final Context context;

    HistoryItemValueProvider(Context context) {
        this.context = context;
    }

    @DebugLog
    public static HistoryItemValueProvider of(final History item, Context context) {
        final BillType billType = BillType.valueOf(item.getBillType());
        switch (billType) {
            case PGE_G11:
                return new PgeG11BillHistoryItemValueProvider(item, context);
            case PGE_G12:
                return new PgeG12BillHistoryItemValueProvider(item, context);
            case PGNIG:
                return new PgnigBillHistoryItemValueProvider(item, context);
            case TAURON_G11:
                return new TauronG11BillHistoryItemValueProvider(item, context);
            case TAURON_G12:
                return new TauronG12BillHistoryItemValueProvider(item, context);
        }
        throw new EnumVariantNotHandledException(billType);
    }

    public abstract Bill getBill();

    public abstract int getLogoId();

    public abstract String getDayReadings();

    public String getDatePeriod() {
        return createPeriodString(
                Dates.format(Dates.toLocalDate(getBill().getDateFrom()), Dates.DEFAULT_DATE_PATTERN),
                Dates.format(Dates.toLocalDate(getBill().getDateTo()), Dates.DEFAULT_DATE_PATTERN));
    }

    public String getAmount() {
        final Double amountToPay = getBill().getAmountToPay();
        final String amountDisplay = Display.toPay(new BigDecimal(amountToPay.toString()));
        return context.getString(R.string.history_amount, amountDisplay);
    }

    protected String createPeriodString(String from, String to) {
        return context.getString(R.string.history_period, from, to);
    }
}