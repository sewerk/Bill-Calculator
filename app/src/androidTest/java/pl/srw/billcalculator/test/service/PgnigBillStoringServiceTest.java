package pl.srw.billcalculator.test.service;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ServiceTestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import pl.srw.billcalculator.db.PgnigBill;
import pl.srw.billcalculator.db.PgnigPrices;
import pl.srw.billcalculator.intent.IntentCreator;
import pl.srw.billcalculator.persistence.Database;
import pl.srw.billcalculator.util.Dates;
import pl.srw.billcalculator.service.PgnigBillStoringService;

/**
 * Created by Kamil Seweryn.
 */
@RunWith(AndroidJUnit4.class)
public class PgnigBillStoringServiceTest extends ServiceTestCase<PgnigBillStoringServiceTest.PgnigBillStoringServiceWrapper> {

    public PgnigBillStoringServiceTest() {
        super(PgnigBillStoringServiceWrapper.class);
    }

    public static class PgnigBillStoringServiceWrapper extends PgnigBillStoringService {

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

        Database.initialize(getSystemContext());
        Database.getSession().deleteAll(PgnigBill.class);
    }

    @Override
    protected void setupService() {
        super.setupService();

        latch = new CountDownLatch(1);
        getService().setLatch(latch);
    }

    @Test
    public void shouldStoreBillInHistory() throws InterruptedException {
        final Intent intent = new Intent(getContext(), PgnigBillStoringService.class);
        intent.putExtra(IntentCreator.READING_FROM, 1);
        intent.putExtra(IntentCreator.READING_TO, 4);
        intent.putExtra(IntentCreator.DATE_FROM, "01/01/2014");
        intent.putExtra(IntentCreator.DATE_TO, "21/12/2015");
        startService(intent);

        latch.await();

        // verify
        final List<PgnigBill> bills = Database.getSession().getPgnigBillDao().loadAll();
        assertEquals(1, bills.size());
        final PgnigBill bill = bills.get(0);
        assertEquals(1, bill.getReadingFrom().intValue());
        assertEquals(4, bill.getReadingTo().intValue());
        assertEquals("01/01/2014", Dates.format(Dates.toLocalDate(bill.getDateFrom())));
        assertEquals("21/12/2015", Dates.format(Dates.toLocalDate(bill.getDateTo())));
        assertNotNull(bill.getAmountToPay());
        final PgnigPrices prices = bill.getPgnigPrices();
        assertTrue(prices.getId() > 0);
        assertFalse(prices.getOplataAbonamentowa().isEmpty());

    }
}
