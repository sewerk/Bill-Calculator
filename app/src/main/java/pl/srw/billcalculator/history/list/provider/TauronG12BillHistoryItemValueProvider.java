package pl.srw.billcalculator.history.list.provider;

import android.content.Intent;
import android.support.annotation.Size;

import lombok.ToString;
import pl.srw.billcalculator.BillCalculator;
import pl.srw.billcalculator.db.Bill;
import pl.srw.billcalculator.db.History;
import pl.srw.billcalculator.db.TauronG12Bill;
import pl.srw.billcalculator.db.dao.HistoryDao;
import pl.srw.billcalculator.db.dao.TauronG12BillDao;
import pl.srw.billcalculator.intent.BillActivityIntentFactory;
import pl.srw.billcalculator.persistence.exception.DbRelationMissingException;
import pl.srw.billcalculator.persistence.type.BillType;
import pl.srw.billcalculator.type.Provider;

/**
 * Created by kseweryn on 03.06.15.
 */
@ToString
public class TauronG12BillHistoryItemValueProvider extends HistoryItemValueProvider {

    private final TauronG12Bill bill;

    public TauronG12BillHistoryItemValueProvider(final History item) {
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
    public @Size(value = 4) int[] getReadings() {
        return new int[]{
                bill.getReadingDayFrom(), bill.getReadingDayTo(),
                bill.getReadingNightFrom(), bill.getReadingNightTo()
        };
    }

    @Override
    public Intent getIntent() {
        return BillActivityIntentFactory.of(BillCalculator.context, Provider.TAURON).from(bill);
    }
}
