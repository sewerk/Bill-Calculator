package pl.srw.billcalculator.service;

import android.app.IntentService;
import android.content.Intent;

import pl.srw.billcalculator.calculation.PgeG11CalculatedBill;
import pl.srw.billcalculator.calculation.PgeG12CalculatedBill;
import pl.srw.billcalculator.db.PgeG11Bill;
import pl.srw.billcalculator.db.PgeG12Bill;
import pl.srw.billcalculator.intent.IntentCreator;
import pl.srw.billcalculator.persistence.Database;
import pl.srw.billcalculator.preference.PgePrices;
import pl.srw.billcalculator.util.Dates;

/**
 * Created by Kamil Seweryn.
 */
public class PgeBillStoringService extends IntentService {

    public PgeBillStoringService() {
        super("PgeBiiStoringService");
    }

    @Override
    protected void onHandleIntent(final Intent intent) {

        final String dateFrom = intent.getStringExtra(IntentCreator.DATE_FROM);
        final String dateTo = intent.getStringExtra(IntentCreator.DATE_TO);

        final int readingFrom = intent.getIntExtra(IntentCreator.READING_FROM, -1);
        final int readingTo = intent.getIntExtra(IntentCreator.READING_TO, -1);

        final int readingDayFrom = intent.getIntExtra(IntentCreator.READING_DAY_FROM, -1);
        final int readingDayTo = intent.getIntExtra(IntentCreator.READING_DAY_TO, -1);
        final int readingNightFrom = intent.getIntExtra(IntentCreator.READING_NIGHT_FROM, -1);
        final int readingNightTo = intent.getIntExtra(IntentCreator.READING_NIGHT_TO, -1);

        final PgePrices prices = new PgePrices();
        final pl.srw.billcalculator.db.PgePrices dbPrices = prices.convertToDb();
        Database.getSession().insert(dbPrices);

        if (isTwoUnitTariff(readingDayTo))
            storeG12Bill(readingDayFrom, readingDayTo, readingNightFrom, readingNightTo, dateFrom, dateTo, dbPrices);
        else
            storeG11Bill(readingFrom, readingTo, dateFrom, dateTo, dbPrices);
    }

    private void storeG11Bill(final int readingFrom, final int readingTo,
                              final String dateFrom, final String dateTo,
                              final pl.srw.billcalculator.db.PgePrices dbPrices) {
        final PgeG11CalculatedBill calculatedBill = new PgeG11CalculatedBill(readingFrom, readingTo, dateFrom, dateTo, dbPrices);
        final PgeG11Bill bill = new PgeG11Bill(null, readingFrom, readingTo,
                Dates.toDate(Dates.parse(dateFrom)), Dates.toDate(Dates.parse(dateTo)),
                calculatedBill.getGrossChargeSum().doubleValue(), dbPrices.getId());
        Database.getSession().insert(bill);
    }

    private void storeG12Bill(final int readingDayFrom, final int readingDayTo,
                              final int readingNightFrom, final int readingNightTo,
                              final String dateFrom, final String dateTo,
                              final pl.srw.billcalculator.db.PgePrices dbPrices) {
        final PgeG12CalculatedBill calculatedBill =
                new PgeG12CalculatedBill(readingDayFrom, readingDayTo, readingNightFrom, readingNightTo, dateFrom, dateTo, dbPrices);
        final PgeG12Bill bill = new PgeG12Bill(null, readingDayFrom, readingDayTo, readingNightFrom, readingNightTo,
                Dates.toDate(Dates.parse(dateFrom)), Dates.toDate(Dates.parse(dateTo)),
                calculatedBill.getGrossChargeSum().doubleValue(), dbPrices.getId());
        Database.getSession().insert(bill);
    }

    private boolean isTwoUnitTariff(final int readingDayTo) {
        return readingDayTo > 0;
    }

}
