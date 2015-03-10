package pl.srw.billcalculator.service;

import android.app.IntentService;
import android.content.Intent;

import hugo.weaving.DebugLog;
import pl.srw.billcalculator.calculation.PgnigCalculatedBill;
import pl.srw.billcalculator.db.PgnigBill;
import pl.srw.billcalculator.intent.IntentCreator;
import pl.srw.billcalculator.persistence.Database;
import pl.srw.billcalculator.preference.PgnigPrices;
import pl.srw.billcalculator.util.Dates;

/**
 * Created by Kamil Seweryn.
 */
public class PgnigBillStoringService extends IntentService {

    public PgnigBillStoringService() {
        super("BillStoringService");
    }

    @DebugLog
    @Override
    protected void onHandleIntent(final Intent intent) {

        final String dateFrom = intent.getStringExtra(IntentCreator.DATE_FROM);
        final String dateTo = intent.getStringExtra(IntentCreator.DATE_TO);
        final int readingFrom = intent.getIntExtra(IntentCreator.READING_FROM, 0);
        final int readingTo = intent.getIntExtra(IntentCreator.READING_TO, 0);

        final PgnigPrices prices = new PgnigPrices();
        final PgnigCalculatedBill calculatedBill = new PgnigCalculatedBill(readingFrom, readingTo, dateFrom, dateTo, prices);

        final pl.srw.billcalculator.db.PgnigPrices dbPrices = prices.convertToDb();
        Database.getSession().insert(dbPrices);

        final PgnigBill bill = new PgnigBill(null, readingFrom, readingTo,
                Dates.toDate(Dates.parse(dateFrom)), Dates.toDate(Dates.parse(dateTo)),
                calculatedBill.getGrossChargeSum().doubleValue(), dbPrices.getId());
        Database.getSession().insert(bill);
    }
}
