package pl.srw.billcalculator.history.list.provider;

import android.content.Context;

import pl.srw.billcalculator.db.Bill;
import pl.srw.billcalculator.db.History;
import pl.srw.billcalculator.db.TauronG12Bill;
import pl.srw.billcalculator.db.dao.HistoryDao;
import pl.srw.billcalculator.db.dao.TauronG12BillDao;
import pl.srw.billcalculator.persistence.exception.DbRelationMissingException;
import pl.srw.billcalculator.persistence.type.BillType;
import pl.srw.billcalculator.type.Provider;

class TauronG12BillHistoryItemValueProvider extends HistoryItemValueProvider
        implements DoubleReadingsBillHistoryItemValueProviding {

    private final TauronG12Bill bill;

    TauronG12BillHistoryItemValueProvider(final History item, Context context) {
        super(context);
        bill = (TauronG12Bill) BillType.TAURON_G12.getDao().load(item.getBillId());
        if (bill == null)
            throw new DbRelationMissingException(HistoryDao.TABLENAME, TauronG12BillDao.TABLENAME);
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
        return createPeriodString(bill.getReadingDayFrom().toString(), bill.getReadingDayTo().toString());
    }

    @Override
    public String getNightReadings() {
        return createPeriodString(bill.getReadingNightFrom().toString(), bill.getReadingNightTo().toString());
    }
}
