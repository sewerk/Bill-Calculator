package pl.srw.billcalculator.task;

import pl.srw.billcalculator.db.PgeBill;
import pl.srw.billcalculator.db.PgePrices;
import pl.srw.billcalculator.db.dao.PgeBillDao;
import pl.srw.billcalculator.persistence.exception.IncompleteDateException;

/**
 * Created by Kamil Seweryn.
 */
public class PgeBillStorer extends BillStorer {

    private PgeBill entry;

    public PgeBillStorer(final int readingFrom, final int readingTo) {
        entry = new PgeBill();
        entry.setReadingFrom(readingFrom);
        entry.setReadingTo(readingTo);
    }

    @Override
    public PgeBill getEntry() {
        return entry;
    }

    @Override
    public PgePrices getPrices() {
        pl.srw.billcalculator.data.PgePrices pgePrices = pl.srw.billcalculator.data.PgePrices.INSTANCE;

        PgePrices dbPrices = new PgePrices();
        dbPrices.setCenaOplataAbonamentowa(pgePrices.getCenaOplataAbonamentowa().toString());
        dbPrices.setCenaOplataPrzejsciowa(pgePrices.getCenaOplataPrzejsciowa().toString());
        dbPrices.setCenaOplataSieciowa(pgePrices.getCenaOplataSieciowa().toString());
        dbPrices.setCenaOplStalaZaPrzesyl(pgePrices.getCenaOplStalaZaPrzesyl().toString());
        dbPrices.setCenaSkladnikJakosciowy(pgePrices.getCenaSkladnikJakosciowy().toString());
        dbPrices.setCenaZaEnergieCzynna(pgePrices.getCenaZaEnergieCzynna().toString());
        return dbPrices;
    }

    @Override
    protected void validate() {
        if (entry.getReadingTo() == 0
                || entry.getDateFrom() == null
                || entry.getAmountToPay() <= 0.0
                || entry.getPgePrices() == null)
            throw new IncompleteDateException(PgeBillDao.TABLENAME);
    }

    @Override
    protected <T> void assignPricesToBill(final T prices) {
        entry.setPgePrices((PgePrices) prices);
    }
}
