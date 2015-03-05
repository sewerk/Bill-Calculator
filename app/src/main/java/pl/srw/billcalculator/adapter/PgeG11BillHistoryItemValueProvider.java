package pl.srw.billcalculator.adapter;

import android.content.Intent;

import pl.srw.billcalculator.BillCalculator;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.db.Bill;
import pl.srw.billcalculator.db.History;
import pl.srw.billcalculator.db.PgeG11Bill;
import pl.srw.billcalculator.intent.BillActivityIntentFactory;
import pl.srw.billcalculator.persistence.type.BillType;

/**
* Created by Kamil Seweryn.
*/
public class PgeG11BillHistoryItemValueProvider extends HistoryItemValueProvider {

    private final PgeG11Bill bill;

    protected PgeG11BillHistoryItemValueProvider(final History item) {
        bill = (PgeG11Bill) BillType.PGE_G11.getDao().load(item.getBillId());
    }

    @Override
    protected Bill getBill() {
        return bill;
    }

    @Override
    public int getLogoId() {
        return pl.srw.billcalculator.type.BillType.PGE.logoDrawableId;
    }

    @Override
    public String getReadings() {
        return BillCalculator.context.getString(R.string.histiry_readings, bill.getReadingFrom(), bill.getReadingTo());
    }

    @Override
    public Intent getIntent() {
        return BillActivityIntentFactory.of(BillCalculator.context, pl.srw.billcalculator.type.BillType.PGE).from(bill);
    }
}
