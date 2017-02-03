package pl.srw.billcalculator.testutils;

import android.util.Log;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import pl.srw.billcalculator.db.PgeG11Bill;
import pl.srw.billcalculator.db.PgePrices;
import pl.srw.billcalculator.db.dao.DaoSession;
import pl.srw.billcalculator.db.dao.PgeG11BillDao;
import pl.srw.billcalculator.persistence.Database;
import pl.srw.billcalculator.util.Dates;

@Singleton
public final class HistoryGenerator {

    private final pl.srw.billcalculator.settings.prices.PgePrices prefsPrices;

    @Inject
    public HistoryGenerator(pl.srw.billcalculator.settings.prices.PgePrices pgePrices) {
        prefsPrices = pgePrices;
    }

    public void generatePgeG11Bills(final int count) {
        final PgeG11BillDao dao = Database.getSession().getPgeG11BillDao();
        final PgePrices pgePrices = prefsPrices.convertToDb();
        Log.d("", "insert " + count);
        Database.getSession().insert(pgePrices);
        List<PgeG11Bill> pgeBills = new ArrayList<>(count);
        for (int i = 1; i <= count; i++) {
            final int day = (i%335 == 0) ? 1 : i%335 ;
            final Date fromDate = Dates.toDate(LocalDate.ofYearDay(2014, day));
            final Date toDate = Dates.toDate(LocalDate.ofYearDay(2014, day + 30));
            pgeBills.add(new PgeG11Bill(null, i, i+10, fromDate, toDate, i*11.11, pgePrices.getId()));
        }

        dao.insertInTx(pgeBills);
    }

    public void clear() {
Log.d("", "clean");
        final DaoSession session = Database.getSession();
        //bills
        session.getPgnigBillDao().deleteAll();
        session.getPgeG11BillDao().deleteAll();
        session.getPgeG12BillDao().deleteAll();
        session.getTauronG11BillDao().deleteAll();
        session.getTauronG12BillDao().deleteAll();
        //prices
        session.getPgnigPricesDao().deleteAll();
        session.getPgePricesDao().deleteAll();
        session.getTauronPricesDao().deleteAll();
    }

    public void generatePgeG11Bill(int readingTo) {
        final PgeG11BillDao dao = Database.getSession().getPgeG11BillDao();
        final PgePrices pgePrices = prefsPrices.convertToDb();
System.out.println("insert " + pgePrices);
        Database.getSession().insert(pgePrices);
        final int day = (readingTo%335 == 0) ? 1 : readingTo%335 ;
        final Date fromDate = Dates.toDate(LocalDate.ofYearDay(2014, day));
        final Date toDate = Dates.toDate(LocalDate.ofYearDay(2014, day + 30));

        final int readingFrom = (readingTo - 10) < 0 ? 0 : readingTo-10;
        dao.insert(new PgeG11Bill(null, readingFrom, readingTo, fromDate, toDate, readingTo * 11.11, pgePrices.getId()));
    }
}
