package pl.srw.billcalculator.task;

import hugo.weaving.DebugLog;
import pl.srw.billcalculator.db.Bill;
import pl.srw.billcalculator.persistence.Database;
import pl.srw.billcalculator.util.Dates;

/**
 * Created by Kamil Seweryn.
 */
public abstract class BillStorer implements Runnable {

    @DebugLog
    @Override
    public void run() {
        Database.getSession().runInTx(new Runnable() {
            @Override
            public void run() {

                final Object prices = getPrices();
                Database.getSession().insert(prices);
                assignPricesToBill(prices);

                validate();
                Database.getSession().insert(getEntry());
            }
        });
    }

    protected abstract void validate();

    protected abstract <T> void assignPricesToBill(T prices);

    public void putDates(final String dateFrom, final String dateTo) {
        getEntry().setDateFrom(Dates.parse(dateFrom));
        getEntry().setDateTo(Dates.parse(dateTo));
    }

    public void putAmount(final Double amount) {
        getEntry().setAmountToPay(amount);
    }

    protected abstract Bill getEntry();

    protected abstract Object getPrices();


}
