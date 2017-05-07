package pl.srw.billcalculator.bill.service;

import android.app.IntentService;
import android.app.backup.BackupManager;
import android.content.Intent;

import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.f2prateek.dart.Optional;

import org.threeten.bp.LocalDate;

import hugo.weaving.DebugLog;
import pl.srw.billcalculator.bill.calculation.CalculatedBill;
import pl.srw.billcalculator.db.Prices;
import pl.srw.billcalculator.intent.IntentCreator;
import pl.srw.billcalculator.type.Provider;

public abstract class BillStoringService<P extends Prices, C extends CalculatedBill> extends IntentService {

    protected @InjectExtra(IntentCreator.DATE_FROM) LocalDate dateFrom;
    protected @InjectExtra(IntentCreator.DATE_TO) LocalDate dateTo;
    protected @Optional @InjectExtra(IntentCreator.READING_FROM) int readingFrom;
    protected @Optional @InjectExtra(IntentCreator.READING_TO) int readingTo;
    protected @Optional @InjectExtra(IntentCreator.READING_DAY_FROM) int readingDayFrom;
    protected @Optional @InjectExtra(IntentCreator.READING_DAY_TO) int readingDayTo;
    protected @Optional @InjectExtra(IntentCreator.READING_NIGHT_FROM) int readingNightFrom;
    protected @Optional @InjectExtra(IntentCreator.READING_NIGHT_TO) int readingNightTo;

    protected BillStoringService(String name) {
        super(name);
    }

    @DebugLog
    @Override
    protected void onHandleIntent(final Intent intent) {
        Dart.inject(this, intent.getExtras()); // TODO: remove since release crashes after Proguard

        final P prices = storePrices();
        final C calculatedBill = calculateBill(prices);
        storeBill(calculatedBill, prices);

        new BackupManager(this).dataChanged();
    }

    protected abstract Provider getProvider();

    protected abstract void storeBill(C calculatedBill, P prices);

    protected abstract C calculateBill(P prices);

    protected abstract P storePrices();

    protected boolean isTwoUnitTariff() {
        return readingDayTo > 0;
    }

}
