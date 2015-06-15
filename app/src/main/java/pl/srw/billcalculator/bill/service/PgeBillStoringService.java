package pl.srw.billcalculator.bill.service;

import pl.srw.billcalculator.bill.calculation.CalculatedEnergyBill;
import pl.srw.billcalculator.bill.calculation.PgeG11CalculatedBill;
import pl.srw.billcalculator.bill.calculation.PgeG12CalculatedBill;
import pl.srw.billcalculator.db.PgeG11Bill;
import pl.srw.billcalculator.db.PgeG12Bill;
import pl.srw.billcalculator.db.PgePrices;
import pl.srw.billcalculator.persistence.Database;
import pl.srw.billcalculator.type.Provider;
import pl.srw.billcalculator.util.Dates;

/**
 * Created by Kamil Seweryn.
 */
public class PgeBillStoringService extends BillStoringService<PgePrices, CalculatedEnergyBill> {

    public PgeBillStoringService() {
        super("PgeBillStoringService");
    }

    @Override
    protected Provider getProvider() {
        return Provider.PGE;
    }

    @Override
    protected void storeBill(CalculatedEnergyBill calculatedBill, PgePrices prices) {
        if (isTwoUnitTariff()) {
            final PgeG12Bill bill = new PgeG12Bill(null, readingDayFrom, readingDayTo, readingNightFrom, readingNightTo,
                    Dates.toDate(Dates.parse(dateFrom)), Dates.toDate(Dates.parse(dateTo)),
                    calculatedBill.getGrossChargeSum().doubleValue(), prices.getId());
            Database.getSession().insert(bill);
        } else {
            final PgeG11Bill bill = new PgeG11Bill(null, readingFrom, readingTo,
                    Dates.toDate(Dates.parse(dateFrom)), Dates.toDate(Dates.parse(dateTo)),
                    calculatedBill.getGrossChargeSum().doubleValue(), prices.getId());
            Database.getSession().insert(bill);
        }
    }

    @Override
    protected CalculatedEnergyBill calculateBill(PgePrices prices) {
        if (isTwoUnitTariff())
            return new PgeG12CalculatedBill(readingDayFrom, readingDayTo, readingNightFrom, readingNightTo, dateFrom, dateTo, prices);
        else
            return new PgeG11CalculatedBill(readingFrom, readingTo, dateFrom, dateTo, prices);
    }

    @Override
    protected PgePrices storePrices() {
        final PgePrices dbPrices = new pl.srw.billcalculator.settings.prices.PgePrices().convertToDb();
        Database.getSession().insert(dbPrices);
        return dbPrices;
    }
}
