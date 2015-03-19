package pl.srw.billcalculator.test.db;

import android.test.ActivityInstrumentationTestCase2;
import android.test.RenamingDelegatingContext;
import android.widget.EditText;

import java.util.List;

import pl.srw.billcalculator.MainActivity;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.db.PgeG12Bill;
import pl.srw.billcalculator.db.PgePrices;
import pl.srw.billcalculator.persistence.Database;
import pl.srw.billcalculator.testutils.PreferenceUtil;

/**
 * Created by Kamil Seweryn.
 */
public class PgeBillActivityInDbTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity sut;

    public PgeBillActivityInDbTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        RenamingDelegatingContext newContext = new RenamingDelegatingContext(getInstrumentation().getContext(), "test_");
        Database.initialize(newContext);
        Database.getSession().deleteAll(PgeG12Bill.class);

        PreferenceUtil.changeToG12Tariff(getInstrumentation().getTargetContext());
        sut = getActivity();
    }

    public void testPgeG12BillStoredInHistory() throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((EditText)sut.findViewById(R.id.editText_reading_day_from)).setText("123");
                ((EditText)sut.findViewById(R.id.editText_reading_day_to)).setText("321");
                ((EditText)sut.findViewById(R.id.editText_reading_night_from)).setText("234");
                ((EditText)sut.findViewById(R.id.editText_reading_night_to)).setText("432");

                sut.findViewById(R.id.button_calculate).performClick();
            }
        });
        getInstrumentation().waitForIdleSync();

        final List<PgeG12Bill> bills = Database.getSession().getPgeG12BillDao().loadAll();
        assertEquals(1, bills.size());
        final PgeG12Bill bill = bills.get(0);
        assertEquals(123, bill.getReadingDayFrom().intValue());
        assertEquals(321, bill.getReadingDayTo().intValue());
        assertEquals(234, bill.getReadingNightFrom().intValue());
        assertEquals(432, bill.getReadingNightTo().intValue());

        assertNotNull(bill.getDateFrom());
        assertNotNull(bill.getDateTo());
        assertNotNull(bill.getAmountToPay());
        final PgePrices prices = bill.getPgePrices();
        assertTrue(prices.getId() > 0);
        assertFalse(prices.getZaEnergieCzynnaDzien().isEmpty());
    }
}
