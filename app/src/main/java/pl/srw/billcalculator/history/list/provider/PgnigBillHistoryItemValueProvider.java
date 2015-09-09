package pl.srw.billcalculator.history.list.provider;

import android.content.Intent;
import android.support.annotation.Size;

import lombok.ToString;
import pl.srw.billcalculator.BillCalculator;
import pl.srw.billcalculator.db.History;
import pl.srw.billcalculator.db.PgnigBill;
import pl.srw.billcalculator.db.dao.HistoryDao;
import pl.srw.billcalculator.db.dao.PgnigBillDao;
import pl.srw.billcalculator.intent.BillActivityIntentFactory;
import pl.srw.billcalculator.persistence.exception.DbRelationMissingException;
import pl.srw.billcalculator.persistence.type.BillType;
import pl.srw.billcalculator.type.Provider;

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
        return Provider.PGNIG.logoSmallRes;
    }

    @Override
    public @Size(value = 2) int[] getReadings() {
        return new int[]{bill.getReadingFrom(), bill.getReadingTo()};
    }

    @Override
    public Intent getIntent() {
        return BillActivityIntentFactory.of(BillCalculator.context, Provider.PGNIG).from(bill);
    }
}
