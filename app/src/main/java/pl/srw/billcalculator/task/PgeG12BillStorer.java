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
public class PgeG12BillStorer implements Runnable {

    private PgeG12Bill entry;

    public PgeG12BillStorer() {
        entry = new PgeG12Bill();
    }

    @DebugLog
    @Override
    public void run() {
        putPrices();
        validate();

        final PgeG12BillDao dao = Database.getPgeG12BillDao();
        dao.insert(entry);
    }

    private void validate() {
        if (entry.getReadingDayTo() <= 0
                || entry.getReadingNightFrom() <= 0
                || entry.getDateFrom() == null
                //|| entry.getPgeG12Prices().getCenaZaEnergieCzynnaDzien().isEmpty() TODO:
                )
            throw new IncompleteDateException(PgeG12BillDao.TABLENAME);
    }

    public void putReadings(final int readingDayFrom, final int readingDayTo, final int readingNightFrom, final int readingNightTo) {
        entry.setReadingDayFrom(readingDayFrom);
        entry.setReadingDayTo(readingDayTo);
        entry.setReadingNightFrom(readingNightFrom);
        entry.setReadingNightTo(readingNightTo);
    }

    public void putDates(final String dateFrom, final String dateTo) {
        entry.setDateFrom(Dates.parse(dateFrom));
        entry.setDateTo(Dates.parse(dateTo));
    }

    private void putPrices() {
        final PgePrices prices = PgePrices.INSTANCE;
        PgeG12Prices dbStoringPrices = new PgeG12Prices();
        dbStoringPrices.setCenaOplataSieciowaDzien(prices.getCenaOplataSieciowaDzien().toString());
        dbStoringPrices.setCenaOplataSieciowaNoc(prices.getCenaOplataSieciowaNoc().toString());
        dbStoringPrices.setCenaZaEnergieCzynnaDzien(prices.getCenaZaEnergieCzynnaDzien().toString());
        dbStoringPrices.setCenaZaEnergieCzynnaNoc(prices.getCenaZaEnergieCzynnaNoc().toString());

        dbStoringPrices.setCenaSkladnikJakosciowy(prices.getCenaSkladnikJakosciowy().toString());
        dbStoringPrices.setCenaOplataAbonamentowa(prices.getCenaOplataAbonamentowa().toString());
        dbStoringPrices.setCenaOplataPrzejsciowa(prices.getCenaOplataPrzejsciowa().toString());
        dbStoringPrices.setCenaOplStalaZaPrzesyl(prices.getCenaOplStalaZaPrzesyl().toString());

        entry.setPgeG12Prices(dbStoringPrices);
    }
}
