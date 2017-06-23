package pl.srw.billcalculator.bill.service;

import javax.inject.Inject;

import pl.srw.billcalculator.bill.SavedBillsRegistry;
import pl.srw.billcalculator.bill.calculation.PgnigCalculatedBill;
import pl.srw.billcalculator.db.PgnigBill;
import pl.srw.billcalculator.db.PgnigPrices;
import pl.srw.billcalculator.persistence.Database;
import pl.srw.billcalculator.type.Provider;
import pl.srw.billcalculator.util.Dates;
import pl.srw.billcalculator.wrapper.Dependencies;

public class PgnigBillStoringService extends BillStoringService<PgnigPrices, PgnigCalculatedBill> {

    @Inject pl.srw.billcalculator.settings.prices.PgnigPrices prices;
    @Inject SavedBillsRegistry savedBillsRegistry;

    public PgnigBillStoringService() {
        super("BillStoringService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Dependencies.inject(this);
    }

    @Override
    protected Provider getProvider() {
        return Provider.PGNIG;
    }

    @Override
    protected void storeBill(PgnigCalculatedBill calculatedBill, PgnigPrices prices) {
        final PgnigBill bill = new PgnigBill(null, readingFrom, readingTo,
                Dates.toDate(dateFrom), Dates.toDate(dateTo),
                calculatedBill.getGrossChargeSum().doubleValue(), prices.getId());
        Database.getSession().insert(bill);
        savedBillsRegistry.register(bill);
    }

    @Override
    protected PgnigCalculatedBill calculateBill(PgnigPrices prices) {
        return new PgnigCalculatedBill(readingFrom, readingTo, dateFrom, dateTo, prices);
    }

    @Override
    protected PgnigPrices storePrices() {
        final PgnigPrices dbPrices = prices.convertToDb();
        Database.getSession().insert(dbPrices);
        return dbPrices;
    }
}
