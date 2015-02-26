package pl.srw.billcalculator.adapter;

import android.content.Intent;

import pl.srw.billcalculator.BillCalculator;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.db.Bill;
import pl.srw.billcalculator.db.History;
import pl.srw.billcalculator.db.PgeBill;
import pl.srw.billcalculator.intent.BillActivityIntentFactory;
import pl.srw.billcalculator.persistence.type.BillType;

/**
* Created by Kamil Seweryn.
*/
public class PgeBillHistoryItemValueProvider extends HistoryItemValueProvider {

    private final PgeBill bill;

    protected PgeBillHistoryItemValueProvider(final History item) {
        bill = (PgeBill) BillType.PGE.getDao().load(item.getBillId());
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
