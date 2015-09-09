package pl.srw.billcalculator.history.list.provider;

import android.content.Intent;
import android.support.annotation.Size;

import lombok.ToString;
import pl.srw.billcalculator.BillCalculator;
import pl.srw.billcalculator.db.Bill;
import pl.srw.billcalculator.db.History;
import pl.srw.billcalculator.db.PgeG11Bill;
import pl.srw.billcalculator.db.dao.HistoryDao;
import pl.srw.billcalculator.db.dao.PgeG11BillDao;
import pl.srw.billcalculator.intent.BillActivityIntentFactory;
import pl.srw.billcalculator.persistence.exception.DbRelationMissingException;
import pl.srw.billcalculator.persistence.type.BillType;
import pl.srw.billcalculator.type.Provider;

/**
* Created by Kamil Seweryn.
*/
@ToString
public class PgeG11BillHistoryItemValueProvider extends HistoryItemValueProvider {

    private final PgeG11Bill bill;

    protected PgeG11BillHistoryItemValueProvider(final History item) {
        bill = (PgeG11Bill) BillType.PGE_G11.getDao().load(item.getBillId());
        if (bill == null)
            throw new DbRelationMissingException(HistoryDao.TABLENAME, PgeG11BillDao.TABLENAME);
    }

    @Override
    public Bill getBill() {
        return bill;
    }

    @Override
    public int getLogoId() {
        return Provider.PGE.logoSmallRes;
    }

    @Override
    public @Size(value = 2) int[] getReadings() {
        return new int[]{bill.getReadingFrom(), bill.getReadingTo()};
    }

    @Override
    public Intent getIntent() {
        return BillActivityIntentFactory.of(BillCalculator.context, Provider.PGE).from(bill);
    }
}
