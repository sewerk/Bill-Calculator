package pl.srw.billcalculator.test.db;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.RenamingDelegatingContext;

import java.util.List;

import pl.srw.billcalculator.PgeBillActivity;
import pl.srw.billcalculator.db.PgeG12Bill;
import pl.srw.billcalculator.db.PgePrices;
import pl.srw.billcalculator.intent.IntentCreator;
import pl.srw.billcalculator.persistence.Database;
import pl.srw.billcalculator.util.Dates;

/**
 * Created by Kamil Seweryn.
 */
public class PgeBillActivityInDbTest extends ActivityInstrumentationTestCase2<PgeBillActivity> {

    private PgeBillActivity sut;

    public PgeBillActivityInDbTest() {
        super(PgeBillActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        RenamingDelegatingContext newContext = new RenamingDelegatingContext(getInstrumentation().getContext(), "test_");
        Database.initialize(newContext);
        Database.getSession().deleteAll(PgeG12Bill.class);
        Intent intent = new Intent();
        intent.putExtra(IntentCreator.READING_DAY_FROM, 1);
        intent.putExtra(IntentCreator.READING_DAY_TO, 2);
        intent.putExtra(IntentCreator.READING_NIGHT_FROM, 3);
        intent.putExtra(IntentCreator.READING_NIGHT_TO, 4);
        intent.putExtra(IntentCreator.DATE_FROM, "01/01/2014");
        intent.putExtra(IntentCreator.DATE_TO, "21/12/2015");
        setActivityIntent(intent);
        sut = getActivity();
    }

    public void testPgeBillStoredInHistory() {
        getInstrumentation().waitForIdleSync();

        final List<PgeG12Bill> bills = Database.getSession().getPgeG12BillDao().loadAll();
        assertEquals(1, bills.size());
        final PgeG12Bill bill = bills.get(0);
        assertEquals(1, bill.getReadingDayFrom().intValue());
        assertEquals(4, bill.getReadingNightTo().intValue());
        assertEquals("21/12/2015", Dates.format(Dates.toLocalDate(bill.getDateTo())));
        assertNotNull(bill.getAmountToPay());
        final PgePrices prices = bill.getPgePrices();
        assertTrue(prices.getId() > 0);
        assertFalse(prices.getZaEnergieCzynnaDzien().isEmpty());
    }
}
