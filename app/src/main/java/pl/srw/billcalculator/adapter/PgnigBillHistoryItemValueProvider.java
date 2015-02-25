package pl.srw.billcalculator.adapter;

import android.content.Intent;

import pl.srw.billcalculator.BillCalculator;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.db.History;
import pl.srw.billcalculator.db.PgnigBill;
import pl.srw.billcalculator.intent.IntentFactory;
import pl.srw.billcalculator.persistence.type.BillType;

/**
 * Created by Kamil Seweryn.
 */
public class PgnigBillHistoryItemValueProvider extends HistoryItemValueProvider {

    private final PgnigBill bill;

    protected PgnigBillHistoryItemValueProvider(final History item) {
        bill = (PgnigBill) BillType.PGNIG.getDao().load(item.getBillId());
    }

    @Override
    public PgnigBill getBill() {
        return bill;
    }

    @Override
    public int getLogoId() {
        return pl.srw.billcalculator.type.BillType.PGNIG.logoDrawableId;
    }

    @Override
    public String getReadings() {
        return BillCalculator.context.getString(R.string.histiry_readings, bill.getReadingFrom(), bill.getReadingTo());
    }

    @Override
    public Intent getIntent() {
        return IntentFactory.of(BillCalculator.context, pl.srw.billcalculator.type.BillType.PGNIG).from(bill);
    }
}
