package pl.srw.billcalculator.bill.activity;

import android.content.Intent;

import pl.srw.billcalculator.bill.calculation.CalculatedEnergyBill;
import pl.srw.billcalculator.intent.IntentCreator;
import pl.srw.mfvp.di.MvpComponent;

public abstract class EnergyBillActivity<T extends MvpComponent> extends BillActivity<T> {
    protected CalculatedEnergyBill bill;
    protected int readingDayFrom;
    protected int readingDayTo;
    protected int readingNightFrom;
    protected int readingNightTo;

    protected boolean isTwoUnitTariff() {
        return readingDayTo > 0;
    }

    @Override
    protected void readExtraFrom(Intent intent) {
        super.readExtraFrom(intent);
        readingDayFrom = intent.getIntExtra(IntentCreator.READING_DAY_FROM, 0);
        readingDayTo = intent.getIntExtra(IntentCreator.READING_DAY_TO, 0);
        readingNightFrom = intent.getIntExtra(IntentCreator.READING_NIGHT_FROM, 0);
        readingNightTo = intent.getIntExtra(IntentCreator.READING_NIGHT_TO, 0);
    }
}
