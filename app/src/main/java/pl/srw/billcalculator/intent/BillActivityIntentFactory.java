package pl.srw.billcalculator.intent;

import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import pl.srw.billcalculator.PgeBillActivity;
import pl.srw.billcalculator.PgnigBillActivity;
import pl.srw.billcalculator.db.Bill;
import pl.srw.billcalculator.db.PgeBill;
import pl.srw.billcalculator.db.PgeG12Bill;
import pl.srw.billcalculator.db.PgnigBill;
import pl.srw.billcalculator.type.BillType;
import pl.srw.billcalculator.util.Dates;

/**
 * Created by Kamil Seweryn.
 */
public final class BillActivityIntentFactory {

    public static final String READING_FROM = "READING_FROM";
    public static final String READING_TO = "READING_TO";
    public static final String READING_DAY_FROM = "READING_DAY_FROM";
    public static final String READING_DAY_TO = "READING_DAY_TO";
    public static final String READING_NIGHT_FROM = "READING_NIGHT_FROM";
    public static final String READING_NIGHT_TO = "READING_NIGHT_TO";
    public static final String DATE_FROM = "DATE_FROM";
    public static final String DATE_TO = "DATE_TO";
    public static final String PRICES = "PRICES";


    private BillActivityIntentFactory() {}

    public static IntentCreator of(final Context context, final BillType billType) {
        switch (billType) {
            case PGE:
                return new IntentCreator(context, PgeBillActivity.class);
            case PGNIG:
                return new IntentCreator(context, PgnigBillActivity.class);
        }
        throw new RuntimeException("Type " + billType + " is not handled.");
    }

    public static class IntentCreator {

        private final Intent intent;

        private IntentCreator(final Context context, final Class<?> aClass) {
            intent = new Intent(context, aClass);
        }

        public Intent from(final EditText etReadingFrom, final EditText etReadingTo,
                           final Button bDateFrom, final Button bDateTo) {
            return from(getIntText(etReadingFrom), getIntText(etReadingTo),
                    getStringText(bDateFrom), getStringText(bDateTo));
        }

        public Intent from(int readingFrom, int readingTo, String dateFrom, String dateTo) {
            putReadingsExtra(readingFrom, readingTo);
            putDatesExtra(dateFrom, dateTo);
            return intent;
        }

        public Intent from(final EditText etReadingDayFrom, final EditText etReadingDayTo,
                           final EditText etReadingNightFrom, final EditText etReadingNightTo,
                           final Button bDateFrom, final Button bDateTo) {
            return from(getIntText(etReadingDayFrom), getIntText(etReadingDayTo),
                    getIntText(etReadingNightFrom), getIntText(etReadingNightTo),
                    getStringText(bDateFrom), getStringText(bDateTo));
        }

        public Intent from(int readingDayFrom, int readingDayTo, int readingNightFrom, int readingNightTo, String dateFrom, String dateTo) {
            putReadingsG12Extra(readingDayFrom, readingDayTo, readingNightFrom, readingNightTo);
            putDatesExtra(dateFrom, dateTo);
            return intent;
        }

        public Intent from(final PgeBill bill) {
            putDatesExtra(bill);
            putReadingsExtra(bill.getReadingFrom(), bill.getReadingTo());
            intent.putExtra(PRICES, bill.getPgePrices());
            return intent;
        }

        public Intent from(final PgeG12Bill bill) {
            putDatesExtra(bill);
            putReadingsG12Extra(bill.getReadingDayFrom(), bill.getReadingDayTo(), bill.getReadingNightFrom(), bill.getReadingNightTo());
            intent.putExtra(PRICES, bill.getPgePrices());
            return intent;
        }

        public Intent from(final PgnigBill bill) {
            putDatesExtra(bill);
            putReadingsExtra(bill.getReadingFrom(), bill.getReadingTo());
            intent.putExtra(PRICES, bill.getPgnigPrices());
            return intent;
        }

        private void putDatesExtra(final Bill bill) {
            final String dateFrom = Dates.format(Dates.toLocalDate(bill.getDateFrom()));
            final String dateTo = Dates.format(Dates.toLocalDate(bill.getDateTo()));
            putDatesExtra(dateFrom, dateTo);
        }

        private void putReadingsExtra(final int readingFrom, final int readingTo) {
            intent.putExtra(READING_FROM, readingFrom);
            intent.putExtra(READING_TO, readingTo);
        }

        private void putReadingsG12Extra(final int readingDayFrom, final int readingDayTo, final int readingNightFrom, final int readingNightTo) {
            intent.putExtra(READING_DAY_FROM, readingDayFrom);
            intent.putExtra(READING_DAY_TO, readingDayTo);
            intent.putExtra(READING_NIGHT_FROM, readingNightFrom);
            intent.putExtra(READING_NIGHT_TO, readingNightTo);
        }

        private void putDatesExtra(final String dateFrom, final String dateTo) {
            intent.putExtra(DATE_FROM, dateFrom);
            intent.putExtra(DATE_TO, dateTo);
        }

        private int getIntText(final TextView textView) {
            return Integer.parseInt(getStringText(textView));
        }

        private String getStringText(final TextView textView) {
            return textView.getText().toString();
        }
    }
}
