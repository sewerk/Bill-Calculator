package pl.srw.billcalculator.bill.activity;

import com.f2prateek.dart.InjectExtra;
import com.f2prateek.dart.Optional;

import pl.srw.billcalculator.bill.calculation.CalculatedEnergyBill;
import pl.srw.billcalculator.intent.IntentCreator;

/**
 * Created by kseweryn on 08.06.15.
 */
public abstract class EnergyBillActivity extends BillActivity {
    protected CalculatedEnergyBill bill;
    protected @InjectExtra(IntentCreator.DATE_FROM) String dateFrom;
    protected @InjectExtra(IntentCreator.DATE_TO) String dateTo;
    protected @Optional @InjectExtra(IntentCreator.READING_FROM) int readingFrom;
    protected @Optional @InjectExtra(IntentCreator.READING_TO) int readingTo;
    protected @Optional @InjectExtra(IntentCreator.READING_DAY_FROM) int readingDayFrom;
    protected @Optional @InjectExtra(IntentCreator.READING_DAY_TO) int readingDayTo;
    protected @Optional @InjectExtra(IntentCreator.READING_NIGHT_FROM) int readingNightFrom;
    protected @Optional @InjectExtra(IntentCreator.READING_NIGHT_TO) int readingNightTo;

    protected boolean isTwoUnitTariff() {
        return readingDayTo > 0;
    }
}
