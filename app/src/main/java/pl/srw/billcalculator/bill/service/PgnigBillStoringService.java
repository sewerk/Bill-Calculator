package pl.srw.billcalculator.bill.service;

import pl.srw.billcalculator.bill.SavedBillsRegistry;
import pl.srw.billcalculator.bill.calculation.PgnigCalculatedBill;
import pl.srw.billcalculator.db.PgnigBill;
import pl.srw.billcalculator.db.PgnigPrices;
import pl.srw.billcalculator.persistence.Database;
import pl.srw.billcalculator.type.Provider;
import pl.srw.billcalculator.util.Dates;

/**
 * Created by Kamil Seweryn.
 */
public class PgnigBillStoringService extends BillStoringService<PgnigPrices, PgnigCalculatedBill> {

    public PgnigBillStoringService() {
        super("BillStoringService");
    }

    @Override
    protected Provider getProvider() {
        return Provider.PGNIG;
    }

    @Override
    protected void storeBill(PgnigCalculatedBill calculatedBill, PgnigPrices prices) {
        final PgnigBill bill = new PgnigBill(null, readingFrom, readingTo,
                Dates.toDate(Dates.parse(dateFrom)), Dates.toDate(Dates.parse(dateTo)),
                calculatedBill.getGrossChargeSum().doubleValue(), prices.getId());
        Database.getSession().insert(bill);
        SavedBillsRegistry.getInstance().register(bill);
    }

    @Override
    protected PgnigCalculatedBill calculateBill(PgnigPrices prices) {
        return new PgnigCalculatedBill(readingFrom, readingTo, dateFrom, dateTo, prices);
    }

    @Override
    protected PgnigPrices storePrices() {
        final PgnigPrices dbPrices = new pl.srw.billcalculator.settings.prices.PgnigPrices().convertToDb();
        Database.getSession().insert(dbPrices);
        return dbPrices;
    }
}
