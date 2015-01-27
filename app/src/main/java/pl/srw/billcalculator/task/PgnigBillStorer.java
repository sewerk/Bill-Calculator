package pl.srw.billcalculator.task;

import pl.srw.billcalculator.db.Bill;
import pl.srw.billcalculator.db.PgnigBill;
import pl.srw.billcalculator.db.PgnigPrices;
import pl.srw.billcalculator.db.dao.PgnigBillDao;
import pl.srw.billcalculator.persistence.exception.IncompleteDateException;

/**
 * Created by Kamil Seweryn.
 */
public class PgnigBillStorer extends BillStorer {

    private final PgnigBill entry;

    public PgnigBillStorer(final int readingFrom, final int readingTo) {
        entry = new PgnigBill();
        entry.setReadingFrom(readingFrom);
        entry.setReadingTo(readingTo);
    }

    @Override
    protected void validate() {
        if (entry.getReadingTo() == 0
                || entry.getDateFrom() == null
                || entry.getAmountToPay() <= 0.0
                || entry.getPgnigPrices() == null)
            throw new IncompleteDateException(PgnigBillDao.TABLENAME);
    }

    @Override
    protected <T> void assignPricesToBill(final T prices) {
        entry.setPgnigPrices((PgnigPrices) prices);
    }

    @Override
    protected Bill getEntry() {
        return entry;
    }

    @Override
    protected PgnigPrices getPrices() {
        final pl.srw.billcalculator.preference.PgnigPrices prices =
                new pl.srw.billcalculator.preference.PgnigPrices(context);
        return prices.convertToDb();
    }
}
