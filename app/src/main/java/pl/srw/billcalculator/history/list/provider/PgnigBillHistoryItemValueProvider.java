package pl.srw.billcalculator.history.list.provider;

import android.content.Intent;

import lombok.ToString;
import pl.srw.billcalculator.BillCalculator;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.db.History;
import pl.srw.billcalculator.db.PgnigBill;
import pl.srw.billcalculator.db.dao.HistoryDao;
import pl.srw.billcalculator.db.dao.PgnigBillDao;
import pl.srw.billcalculator.intent.BillActivityIntentFactory;
import pl.srw.billcalculator.persistence.exception.DbRelationMissingException;
import pl.srw.billcalculator.persistence.type.BillType;

/**
 * Created by Kamil Seweryn.
 */
@ToString
public class PgnigBillHistoryItemValueProvider extends HistoryItemValueProvider {

    private final PgnigBill bill;

    protected PgnigBillHistoryItemValueProvider(final History item) {
        bill = (PgnigBill) BillType.PGNIG.getDao().load(item.getBillId());
        if (bill == null)
            throw new DbRelationMissingException(HistoryDao.TABLENAME, PgnigBillDao.TABLENAME);
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
        return BillActivityIntentFactory.of(BillCalculator.context, pl.srw.billcalculator.type.BillType.PGNIG).from(bill);
    }
}
