package pl.srw.billcalculator.test.generator;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pl.srw.billcalculator.db.PgeG11Bill;
import pl.srw.billcalculator.db.PgePrices;
import pl.srw.billcalculator.db.dao.PgeG11BillDao;
import pl.srw.billcalculator.persistence.Database;
import pl.srw.billcalculator.util.Dates;

/**
 * Created by Kamil Seweryn.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class DummyHistoryGenerator {

    @Test
    public void generate200Bills() throws InterruptedException {
        final PgeG11BillDao pgeG11BillDao = Database.getSession().getPgeG11BillDao();

        final PgePrices pgePrices = new pl.srw.billcalculator.preference.PgePrices().convertToDb();
        Database.getSession().insert(pgePrices);

        List<PgeG11Bill> pgeBills = new ArrayList<>(70);
        for (int i = 0; i < 200; i++) {
            final Date fromDate = Dates.toDate(LocalDate.ofYearDay(2014, i + 1));
            final Date toDate = Dates.toDate(LocalDate.ofYearDay(2014, i + 30));
            pgeBills.add(new PgeG11Bill(null, i, i+10, fromDate, toDate, i*11.11, pgePrices.getId()));
        }

        pgeG11BillDao.insertInTx(pgeBills);
    }
}
