package pl.srw.billcalculator.testutils;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pl.srw.billcalculator.db.PgeG11Bill;
import pl.srw.billcalculator.db.PgePrices;
import pl.srw.billcalculator.db.dao.DaoSession;
import pl.srw.billcalculator.db.dao.PgeG11BillDao;
import pl.srw.billcalculator.persistence.Database;
import pl.srw.billcalculator.util.Dates;

/**
 * Created by Kamil Seweryn.
 */
public final class HistoryGenerator {

    private HistoryGenerator() { }

    public static void generatePgeG11Bills(final int count) {
        final PgeG11BillDao dao = Database.getSession().getPgeG11BillDao();
        final PgePrices pgePrices = new pl.srw.billcalculator.settings.prices.PgePrices().convertToDb();

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

    public static void clear() {
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

    public static void generatePgeG11Bill(int readingTo) {
        final PgeG11BillDao dao = Database.getSession().getPgeG11BillDao();
        final PgePrices pgePrices = new pl.srw.billcalculator.settings.prices.PgePrices().convertToDb();

        Database.getSession().insert(pgePrices);
        final int day = (readingTo%335 == 0) ? 1 : readingTo%335 ;
        final Date fromDate = Dates.toDate(LocalDate.ofYearDay(2014, day));
        final Date toDate = Dates.toDate(LocalDate.ofYearDay(2014, day + 30));

        final int readingFrom = (readingTo - 10) < 0 ? 0 : readingTo-10;
        dao.insert(new PgeG11Bill(null, readingFrom, readingTo, fromDate, toDate, readingTo * 11.11, pgePrices.getId()));
    }
}
