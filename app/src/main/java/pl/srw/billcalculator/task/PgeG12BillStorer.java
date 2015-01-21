package pl.srw.billcalculator.task;

import hugo.weaving.DebugLog;
import pl.srw.billcalculator.data.PgePrices;
import pl.srw.billcalculator.db.PgeG12Bill;
import pl.srw.billcalculator.db.PgeG12Prices;
import pl.srw.billcalculator.db.dao.PgeG12BillDao;
import pl.srw.billcalculator.persistence.Database;
import pl.srw.billcalculator.persistence.exception.IncompleteDateException;
import pl.srw.billcalculator.util.Dates;

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
    public PgeG12Prices getPrices() {
        final PgePrices pgePrices = PgePrices.INSTANCE;

        PgeG12Prices prices = new PgeG12Prices();
        prices.setCenaOplataSieciowaDzien(pgePrices.getCenaOplataSieciowaDzien().toString());
        prices.setCenaOplataSieciowaNoc(pgePrices.getCenaOplataSieciowaNoc().toString());
        prices.setCenaZaEnergieCzynnaDzien(pgePrices.getCenaZaEnergieCzynnaDzien().toString());
        prices.setCenaZaEnergieCzynnaNoc(pgePrices.getCenaZaEnergieCzynnaNoc().toString());

        prices.setCenaSkladnikJakosciowy(pgePrices.getCenaSkladnikJakosciowy().toString());
        prices.setCenaOplataAbonamentowa(pgePrices.getCenaOplataAbonamentowa().toString());
        prices.setCenaOplataPrzejsciowa(pgePrices.getCenaOplataPrzejsciowa().toString());
        prices.setCenaOplStalaZaPrzesyl(pgePrices.getCenaOplStalaZaPrzesyl().toString());

        return prices;
    }

    @Override
    protected void validate() {
        if (entry.getReadingDayTo() == 0
                || entry.getDateFrom() == null
                || entry.getAmountToPay() <= 0.0
                || entry.getPgeG12Prices() == null)
            throw new IncompleteDateException(PgeG12BillDao.TABLENAME);
    }

    @Override
    protected <T> void assignPricesToBill(final T prices) {
        entry.setPgeG12Prices((PgeG12Prices) prices);
    }
}
