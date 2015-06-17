package pl.srw.billcalculator.test.db;

import android.test.ActivityInstrumentationTestCase2;
import android.test.RenamingDelegatingContext;
import android.widget.EditText;

import java.util.List;

import pl.srw.billcalculator.form.MainActivity;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.db.PgnigBill;
import pl.srw.billcalculator.db.PgnigPrices;
import pl.srw.billcalculator.form.view.SlidingTabLayout;
import pl.srw.billcalculator.persistence.Database;
import pl.srw.billcalculator.testutils.PreferenceUtil;

/**
 * Created by Kamil Seweryn.
 */
public class PgnigBillActivityInDbTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity sut;

    public PgnigBillActivityInDbTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        RenamingDelegatingContext newContext = new RenamingDelegatingContext(getInstrumentation().getContext(), "test_");
        Database.initialize(newContext);
        Database.getSession().deleteAll(PgnigBill.class);

        PreferenceUtil.changeToG11Tariff(getInstrumentation().getTargetContext());
        sut = getActivity();
    }

    public void testPgnigBillStoredInHistory() throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((SlidingTabLayout) sut.findViewById(R.id.sliding_tabs)).getChildAt(1).performClick();
            }
        });
        getInstrumentation().waitForIdleSync();
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((EditText) sut.findViewById(R.id.et_reading_from)).setText("123");
                ((EditText) sut.findViewById(R.id.et_reading_to)).setText("321");

                sut.findViewById(R.id.button_calculate).performClick();
            }
        });
        getInstrumentation().waitForIdleSync();

        final List<PgnigBill> bills = Database.getSession().getPgnigBillDao().loadAll();
        assertEquals(1, bills.size());
        final PgnigBill bill = bills.get(0);
        assertEquals(123, bill.getReadingFrom().intValue());
        assertEquals(321, bill.getReadingTo().intValue());
        assertNotNull(bill.getDateFrom());
        assertNotNull(bill.getDateTo());
        assertNotNull(bill.getAmountToPay());
        final PgnigPrices prices = bill.getPgnigPrices();
        assertTrue(prices.getId() > 0);
        assertFalse(prices.getDystrybucyjnaZmienna().isEmpty());
    }
}
