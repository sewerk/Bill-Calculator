package pl.srw.billcalculator.bill.service;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ServiceTestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import pl.srw.billcalculator.db.PgeG11Bill;
import pl.srw.billcalculator.db.PgeG12Bill;
import pl.srw.billcalculator.db.PgePrices;
import pl.srw.billcalculator.db.dao.DaoMaster;
import pl.srw.billcalculator.history.HistoryGenerator;
import pl.srw.billcalculator.intent.IntentCreator;
import pl.srw.billcalculator.persistence.Database;
import pl.srw.billcalculator.util.Dates;

@RunWith(AndroidJUnit4.class)
public class PgeBillStoringServiceTest extends ServiceTestCase<PgeBillStoringServiceTest.PgeBillStoringServiceWrapper> {

    public PgeBillStoringServiceTest() {
        super(PgeBillStoringServiceWrapper.class);
    }

    public static class PgeBillStoringServiceWrapper extends PgeBillStoringService {

        private CountDownLatch latch;

        public void setLatch(final CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        protected void onHandleIntent(final Intent intent) {
            super.onHandleIntent(intent);
            latch.countDown();
        }
    }

    private CountDownLatch latch;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        setContext(InstrumentationRegistry.getInstrumentation().getTargetContext());

        DaoMaster.newDevSession(getContext(), this.getClass().getSimpleName() + ".DB");
    }

    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        HistoryGenerator.clear();
    }

    @Override
    protected void setupService() {
        super.setupService();

        latch = new CountDownLatch(1);
        getService().setLatch(latch);
    }

    @Test
    public void shouldStoreG11BillInHistory() throws InterruptedException {
        final Intent intent = new Intent(getContext(), PgeBillStoringService.class);
        intent.putExtra(IntentCreator.READING_FROM, 2);
        intent.putExtra(IntentCreator.READING_TO, 5);
        intent.putExtra(IntentCreator.DATE_FROM, LocalDate.of(2014, Month.JANUARY, 1));
        intent.putExtra(IntentCreator.DATE_TO, LocalDate.of(2015, Month.DECEMBER, 21));
        startService(intent);

        latch.await();

        // verify
        final List<PgeG11Bill> bills = Database.getSession().getPgeG11BillDao().loadAll();
        assertEquals(1, bills.size());
        final PgeG11Bill bill = bills.get(0);
        assertEquals(2, bill.getReadingFrom().intValue());
        assertEquals(5, bill.getReadingTo().intValue());
        assertEquals("01/01/2014", Dates.format(Dates.toLocalDate(bill.getDateFrom()), Dates.DEFAULT_DATE_PATTERN));
        assertEquals("21/12/2015", Dates.format(Dates.toLocalDate(bill.getDateTo()), Dates.DEFAULT_DATE_PATTERN));
        assertNotNull(bill.getAmountToPay());
        final PgePrices prices = bill.getPgePrices();
        assertTrue(prices.getId() > 0);
        assertFalse(prices.getOplataAbonamentowa().isEmpty());
    }

    @Test
    public void shouldStoreG12BillInHistory() throws InterruptedException {
        final Intent intent = new Intent(getContext(), PgeBillStoringService.class);
        intent.putExtra(IntentCreator.READING_DAY_FROM, 10);
        intent.putExtra(IntentCreator.READING_DAY_TO, 11);
        intent.putExtra(IntentCreator.READING_NIGHT_FROM, 12);
        intent.putExtra(IntentCreator.READING_NIGHT_TO, 13);
        intent.putExtra(IntentCreator.DATE_FROM, LocalDate.of(2014, Month.FEBRUARY, 1));
        intent.putExtra(IntentCreator.DATE_TO, LocalDate.of(2015, Month.OCTOBER, 21));
        startService(intent);

        latch.await();

        // verify
        final List<PgeG12Bill> bills = Database.getSession().getPgeG12BillDao().loadAll();
        assertEquals(1, bills.size());
        final PgeG12Bill bill = bills.get(0);
        assertEquals(10, bill.getReadingDayFrom().intValue());
        assertEquals(11, bill.getReadingDayTo().intValue());
        assertEquals(12, bill.getReadingNightFrom().intValue());
        assertEquals(13, bill.getReadingNightTo().intValue());
        assertEquals("01/02/2014", Dates.format(Dates.toLocalDate(bill.getDateFrom()), Dates.DEFAULT_DATE_PATTERN));
        assertEquals("21/10/2015", Dates.format(Dates.toLocalDate(bill.getDateTo()), Dates.DEFAULT_DATE_PATTERN));
        assertNotNull(bill.getAmountToPay());
        final PgePrices prices = bill.getPgePrices();
        assertTrue(prices.getId() > 0);
        assertFalse(prices.getOplataAbonamentowa().isEmpty());
    }
}