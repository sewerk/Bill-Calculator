package pl.srw.billcalculator.bill.service;

import android.app.IntentService;
import android.app.backup.BackupManager;
import android.content.Intent;

import org.threeten.bp.LocalDate;

import hugo.weaving.DebugLog;
import pl.srw.billcalculator.bill.calculation.CalculatedBill;
import pl.srw.billcalculator.db.Prices;
import pl.srw.billcalculator.intent.IntentCreator;

public abstract class BillStoringService<P extends Prices, C extends CalculatedBill> extends IntentService {

    protected LocalDate dateFrom;
    protected LocalDate dateTo;
    protected int readingFrom;
    protected int readingTo;
    protected int readingDayFrom;
    protected int readingDayTo;
    protected int readingNightFrom;
    protected int readingNightTo;

    protected BillStoringService(String name) {
        super(name);
    }

    @DebugLog
    @Override
    protected void onHandleIntent(final Intent intent) {
        readExtra(intent);

        final P prices = storePrices();
        final C calculatedBill = calculateBill(prices);
        storeBill(calculatedBill, prices);

        new BackupManager(this).dataChanged();
    }

    protected abstract void storeBill(C calculatedBill, P prices);

    protected abstract C calculateBill(P prices);

    protected abstract P storePrices();

    protected boolean isTwoUnitTariff() {
        return readingDayTo > 0;
    }

    private void readExtra(Intent intent) {
        dateFrom = (LocalDate) intent.getSerializableExtra(IntentCreator.DATE_FROM);
        dateTo = (LocalDate) intent.getSerializableExtra(IntentCreator.DATE_TO);
        readingFrom = intent.getIntExtra(IntentCreator.READING_FROM, 0);
        readingTo = intent.getIntExtra(IntentCreator.READING_TO, 0);
        readingDayFrom = intent.getIntExtra(IntentCreator.READING_DAY_FROM, 0);
        readingDayTo = intent.getIntExtra(IntentCreator.READING_DAY_TO, 0);
        readingNightFrom = intent.getIntExtra(IntentCreator.READING_NIGHT_FROM, 0);
        readingNightTo = intent.getIntExtra(IntentCreator.READING_NIGHT_TO, 0);
    }
}
