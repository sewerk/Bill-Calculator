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

    public static void generatePgeG11Bills(final int count) throws InterruptedException {
        final PgeG11BillDao dao = Database.getSession().getPgeG11BillDao();
        final PgePrices pgePrices = new pl.srw.billcalculator.preference.PgePrices().convertToDb();

        Database.getSession().insert(pgePrices);
        List<PgeG11Bill> pgeBills = new ArrayList<>(count);
        for (int i = 1; i <= count; i++) {
            final Date fromDate = Dates.toDate(LocalDate.ofYearDay(2014, i));
            final Date toDate = Dates.toDate(LocalDate.ofYearDay(2014, i + 30));
            pgeBills.add(new PgeG11Bill(null, i, i+10, fromDate, toDate, i*11.11, pgePrices.getId()));
        }

        dao.insertInTx(pgeBills);
    }

    public static void clear() {
        final DaoSession session = Database.getSession();
        session.getPgnigBillDao().deleteAll();
        session.getPgeG11BillDao().deleteAll();
        session.getPgeG12BillDao().deleteAll();
        session.getPgnigPricesDao().deleteAll();
        session.getPgePricesDao().deleteAll();
    }
}
