package pl.srw.billcalculator.history.list.provider;

import android.content.Context;

import pl.srw.billcalculator.db.Bill;
import pl.srw.billcalculator.db.History;
import pl.srw.billcalculator.db.PgeG12Bill;
import pl.srw.billcalculator.db.dao.HistoryDao;
import pl.srw.billcalculator.db.dao.PgeG12BillDao;
import pl.srw.billcalculator.persistence.exception.DbRelationMissingException;
import pl.srw.billcalculator.persistence.type.BillType;
import pl.srw.billcalculator.type.Provider;

class PgeG12BillHistoryItemValueProvider extends HistoryItemValueProvider {

    private final PgeG12Bill bill;

    PgeG12BillHistoryItemValueProvider(final History item, Context context) {
        super(context);
        bill = (PgeG12Bill) BillType.PGE_G12.getDao().load(item.getBillId());
        if (bill == null)
            throw new DbRelationMissingException(HistoryDao.TABLENAME, PgeG12BillDao.TABLENAME);
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
    public boolean hasDoubleReadings() {
        return true;
    }

    @Override
    public String getDayReadings() {
        return createPeriodString(bill.getReadingDayFrom().toString(), bill.getReadingDayTo().toString());
    }

    @Override
    public String getNightReadings() {
        return createPeriodString(bill.getReadingNightFrom().toString(), bill.getReadingNightTo().toString());
    }
}
