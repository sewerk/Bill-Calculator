package pl.srw.billcalculator.history.list.provider;

import android.content.Context;

import pl.srw.billcalculator.db.History;
import pl.srw.billcalculator.db.PgnigBill;
import pl.srw.billcalculator.db.dao.HistoryDao;
import pl.srw.billcalculator.db.dao.PgnigBillDao;
import pl.srw.billcalculator.persistence.exception.DbRelationMissingException;
import pl.srw.billcalculator.persistence.type.BillType;
import pl.srw.billcalculator.type.Provider;

class PgnigBillHistoryItemValueProvider extends HistoryItemValueProvider {

    private final PgnigBill bill;

    PgnigBillHistoryItemValueProvider(final History item, Context context) {
        super(context);
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
    public String getDayReadings() {
        return createPeriodString(bill.getReadingFrom().toString(), bill.getReadingTo().toString());
    }
}
