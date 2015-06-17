package pl.srw.billcalculator.test.db;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.test.ActivityInstrumentationTestCase2;
import android.test.RenamingDelegatingContext;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.EditText;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import pl.srw.billcalculator.form.MainActivity;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.db.PgeG12Bill;
import pl.srw.billcalculator.db.PgePrices;
import pl.srw.billcalculator.persistence.Database;
import pl.srw.billcalculator.testutils.PreferenceUtil;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Kamil Seweryn.
 */
@SmallTest
public class PgeBillActivityInDbTest {

    @Rule public final ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Database.initialize(targetContext);
        Database.getSession().deleteAll(PgeG12Bill.class);

        PreferenceUtil.changeToG12Tariff(targetContext);
    }

    @Test
    public void shouldStoreBillInHistoryOnCalculateAction() throws Throwable {
        activityTestRule.getActivity();
        onView(withId(R.id.editText_reading_day_from)).perform(typeText("123"), closeSoftKeyboard());
        onView(withId(R.id.editText_reading_day_to)).perform(typeText("321"), closeSoftKeyboard());
        onView(withId(R.id.editText_reading_night_from)).perform(typeText("234"), closeSoftKeyboard());
        onView(withId(R.id.editText_reading_night_to)).perform(typeText("432"), closeSoftKeyboard());
        onView(withId(R.id.button_calculate)).perform(click());

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
