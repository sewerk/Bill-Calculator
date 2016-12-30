package pl.srw.billcalculator.history.list.provider;

import android.content.Context;

import pl.srw.billcalculator.db.Bill;
import pl.srw.billcalculator.db.History;
import pl.srw.billcalculator.db.PgeG11Bill;
import pl.srw.billcalculator.db.dao.HistoryDao;
import pl.srw.billcalculator.db.dao.PgeG11BillDao;
import pl.srw.billcalculator.persistence.exception.DbRelationMissingException;
import pl.srw.billcalculator.persistence.type.BillType;
import pl.srw.billcalculator.type.Provider;

class PgeG11BillHistoryItemValueProvider extends HistoryItemValueProvider {

    private final PgeG11Bill bill;

    PgeG11BillHistoryItemValueProvider(final History item, Context context) {
        super(context);
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
    public String getDayReadings() {
        return createPeriodString(bill.getReadingFrom().toString(), bill.getReadingTo().toString());
    }
}
