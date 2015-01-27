package pl.srw.billcalculator.task;

import pl.srw.billcalculator.db.PgeG12Bill;
import pl.srw.billcalculator.db.PgePrices;
import pl.srw.billcalculator.db.dao.PgeG12BillDao;
import pl.srw.billcalculator.persistence.exception.IncompleteDateException;

/**
 * Created by Kamil Seweryn.
 */
public class PgeG12BillStorer extends BillStorer {

    private PgeG12Bill entry;

    public PgeG12BillStorer(final int readingDayFrom, final int readingDayTo,
                            final int readingNightFrom, final int readingNightTo) {
        entry = new PgeG12Bill();
        entry.setReadingDayFrom(readingDayFrom);
        entry.setReadingDayTo(readingDayTo);
        entry.setReadingNightFrom(readingNightFrom);
        entry.setReadingNightTo(readingNightTo);
    }

    @Override
    public PgeG12Bill getEntry() {
        return entry;
    }

    @Override
    public PgePrices getPrices() {
        final pl.srw.billcalculator.preference.PgePrices pgePrices =
                new pl.srw.billcalculator.preference.PgePrices(context);
        return pgePrices.convertToDb();
    }

    @Override
    protected void validate() {
        if (entry.getReadingDayTo() == 0
                || entry.getDateFrom() == null
                || entry.getAmountToPay() <= 0.0
                || entry.getPgePrices() == null)
            throw new IncompleteDateException(PgeG12BillDao.TABLENAME);
    }

    @Override
    protected <T> void assignPricesToBill(final T prices) {
        entry.setPgePrices((PgePrices) prices);
    }
}
