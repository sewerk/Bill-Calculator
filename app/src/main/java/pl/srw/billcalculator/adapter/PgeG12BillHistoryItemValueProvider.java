package pl.srw.billcalculator.adapter;

import android.content.Intent;

import pl.srw.billcalculator.BillCalculator;
import pl.srw.billcalculator.PgeBillActivity;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.db.Bill;
import pl.srw.billcalculator.db.History;
import pl.srw.billcalculator.db.PgeG12Bill;
import pl.srw.billcalculator.persistence.type.BillType;

/**
 * Created by Kamil Seweryn.
 */
public class PgeG12BillHistoryItemValueProvider extends HistoryItemValueProvider {

    private final PgeG12Bill bill;

    protected PgeG12BillHistoryItemValueProvider(final History item) {
        bill = (PgeG12Bill) BillType.PGE_G12.dao.load(item.getBillId());
    }

    @Override
    protected Bill getBill() {
        return bill;
    }

    @Override
    public int getLogoId() {
        return pl.srw.billcalculator.type.BillType.PGE.drawableId;
    }

    @Override
    public String getReadings() {
        return BillCalculator.context.getString(R.string.history_readingsG12,
                bill.getReadingDayFrom(), bill.getReadingDayTo(),
                bill.getReadingNightFrom(), bill.getReadingNightTo());
    }

    @Override
    public Intent getIntent() {
        return PgeBillActivity.newIntent(bill);
    }
}
