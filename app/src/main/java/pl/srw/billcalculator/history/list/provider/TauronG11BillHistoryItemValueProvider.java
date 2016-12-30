package pl.srw.billcalculator.history.list.provider;

import android.content.Context;

import pl.srw.billcalculator.db.Bill;
import pl.srw.billcalculator.db.History;
import pl.srw.billcalculator.db.TauronG11Bill;
import pl.srw.billcalculator.db.dao.HistoryDao;
import pl.srw.billcalculator.db.dao.TauronG11BillDao;
import pl.srw.billcalculator.persistence.exception.DbRelationMissingException;
import pl.srw.billcalculator.persistence.type.BillType;
import pl.srw.billcalculator.type.Provider;

class TauronG11BillHistoryItemValueProvider extends HistoryItemValueProvider {

    private final TauronG11Bill bill;

    TauronG11BillHistoryItemValueProvider(final History item, Context context) {
        super(context);
        bill = (TauronG11Bill) BillType.TAURON_G11.getDao().load(item.getBillId());
        if (bill == null)
            throw new DbRelationMissingException(HistoryDao.TABLENAME, TauronG11BillDao.TABLENAME);
    }

    @Override
    public Bill getBill() {
        return bill;
    }

    @Override
    public int getLogoId() {
        return Provider.TAURON.logoSmallRes;
    }

    @Override
    public String getDayReadings() {
        return createPeriodString(bill.getReadingFrom().toString(), bill.getReadingTo().toString());
    }
}
