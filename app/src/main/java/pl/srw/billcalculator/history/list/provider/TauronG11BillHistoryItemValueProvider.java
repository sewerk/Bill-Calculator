package pl.srw.billcalculator.history.list.provider;

import android.content.Intent;

import lombok.ToString;
import pl.srw.billcalculator.BillCalculator;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.db.Bill;
import pl.srw.billcalculator.db.History;
import pl.srw.billcalculator.db.TauronG11Bill;
import pl.srw.billcalculator.db.dao.HistoryDao;
import pl.srw.billcalculator.db.dao.TauronG11BillDao;
import pl.srw.billcalculator.intent.BillActivityIntentFactory;
import pl.srw.billcalculator.persistence.exception.DbRelationMissingException;
import pl.srw.billcalculator.persistence.type.BillType;
import pl.srw.billcalculator.type.Provider;

/**
 * Created by kseweryn on 03.06.15.
 */
@ToString
public class TauronG11BillHistoryItemValueProvider extends HistoryItemValueProvider {

    private final TauronG11Bill bill;

    public TauronG11BillHistoryItemValueProvider(final History item) {
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
    public String getReadings() {
        return BillCalculator.context.getString(R.string.histiry_readings, bill.getReadingFrom(), bill.getReadingTo());
    }

    @Override
    public Intent getIntent() {
        return BillActivityIntentFactory.of(BillCalculator.context, Provider.TAURON).from(bill);
    }
}
