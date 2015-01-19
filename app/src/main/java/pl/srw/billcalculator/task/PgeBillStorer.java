package pl.srw.billcalculator.task;

import hugo.weaving.DebugLog;
import pl.srw.billcalculator.db.PgeBill;
import pl.srw.billcalculator.db.PgePrices;
import pl.srw.billcalculator.db.dao.PgeBillDao;
import pl.srw.billcalculator.persistence.Database;
import pl.srw.billcalculator.persistence.exception.IncompleteDateException;
import pl.srw.billcalculator.util.Dates;

/**
 * Created by Kamil Seweryn.
 */
public class PgeBillStorer implements Runnable {

    private PgeBill entry;

    public PgeBillStorer() {
        entry = new PgeBill();
    }

    @DebugLog
    @Override
    public void run() {
        putPrices();
        validate();

        final PgeBillDao dao = Database.getPgeBillDao();
        dao.insert(entry);
    }

    private void validate() {
        if (entry.getReadingTo() <= 0
                || entry.getDateFrom() == null
                //|| entry.getPgePrices().getCenaZaEnergieCzynna().isEmpty() TODO
                )
            throw new IncompleteDateException(PgeBillDao.TABLENAME);
    }

    public void putReadings(final int readingFrom, final int readingTo) {
        entry.setReadingFrom(readingFrom);
        entry.setReadingTo(readingTo);
    }

    public void putDates(final String dateFrom, final String dateTo) {
        entry.setDateFrom(Dates.parse(dateFrom));
        entry.setDateTo(Dates.parse(dateTo));
    }

    private void putPrices() {
        pl.srw.billcalculator.data.PgePrices prices = pl.srw.billcalculator.data.PgePrices.INSTANCE;
        PgePrices dbStoringPrices = new PgePrices();
        dbStoringPrices.setCenaOplataAbonamentowa(prices.getCenaOplataAbonamentowa().toString());
        dbStoringPrices.setCenaOplataPrzejsciowa(prices.getCenaOplataPrzejsciowa().toString());
        dbStoringPrices.setCenaOplataSieciowa(prices.getCenaOplataSieciowa().toString());
        dbStoringPrices.setCenaOplStalaZaPrzesyl(prices.getCenaOplStalaZaPrzesyl().toString());
        dbStoringPrices.setCenaSkladnikJakosciowy(prices.getCenaSkladnikJakosciowy().toString());
        dbStoringPrices.setCenaZaEnergieCzynna(prices.getCenaZaEnergieCzynna().toString());
        entry.setPgePrices(dbStoringPrices);
    }
}
