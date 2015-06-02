package pl.srw.billcalculator.bill.service;

import pl.srw.billcalculator.bill.calculation.CalculatedEnergyBill;
import pl.srw.billcalculator.bill.calculation.TauronG11CalculatedBill;
import pl.srw.billcalculator.bill.calculation.TauronG12CalculatedBill;
import pl.srw.billcalculator.db.TauronG11Bill;
import pl.srw.billcalculator.db.TauronG12Bill;
import pl.srw.billcalculator.db.TauronPrices;
import pl.srw.billcalculator.persistence.Database;
import pl.srw.billcalculator.util.Dates;

/**
 * Created by kseweryn on 29.05.15.
 */
public class TauronBillStoringService extends BillStoringService<TauronPrices, CalculatedEnergyBill> {

    public TauronBillStoringService() {
        super("TauronBillStoringService");
    }

    @Override
    protected void storeBill(CalculatedEnergyBill calculatedBill, TauronPrices prices) {
        if (isTwoUnitTariff()) {
            final TauronG12Bill bill = new TauronG12Bill(null, readingDayFrom, readingDayTo, readingNightFrom, readingNightTo,
                    Dates.toDate(Dates.parse(dateFrom)), Dates.toDate(Dates.parse(dateTo)),
                    calculatedBill.getGrossChargeSum().doubleValue(), prices.getId());
            Database.getSession().insert(bill);
        } else {
            final TauronG11Bill bill = new TauronG11Bill(null, readingFrom, readingTo,
                    Dates.toDate(Dates.parse(dateFrom)), Dates.toDate(Dates.parse(dateTo)),
                    calculatedBill.getGrossChargeSum().doubleValue(), prices.getId());
            Database.getSession().insert(bill);
        }
    }

    @Override
    protected CalculatedEnergyBill calculateBill(TauronPrices prices) {
        if (isTwoUnitTariff())
            return new TauronG12CalculatedBill(readingDayFrom, readingDayTo, readingNightFrom, readingNightTo, dateFrom, dateTo, prices);
        else
            return new TauronG11CalculatedBill(readingFrom, readingTo, dateFrom, dateTo, prices);
    }

    @Override
    protected TauronPrices storePrices() {
        final TauronPrices prices = new pl.srw.billcalculator.settings.prices.TauronPrices().convertToDb();
        Database.getSession().insert(prices);
        return prices;
    }
}
