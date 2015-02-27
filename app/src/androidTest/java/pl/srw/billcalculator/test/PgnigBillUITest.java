package pl.srw.billcalculator.test;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.regex.Pattern;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import pl.srw.billcalculator.MainActivity;
import pl.srw.billcalculator.type.BillType;

/**
 * Created by Kamil Seweryn.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class PgnigBillUITest extends ActivityInstrumentationTestCase2<MainActivity> {

    public PgnigBillUITest() {
        super(MainActivity.class);
    }

    private Solo solo;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @After
    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }

    @Test
    public void shouldCalculateBillWithVatAndPrices() {
        // TODO: set prices
        // wsp. konw. 11.094
        // paliwo 0.11815
        // abon. 6.97
        // dyst. st. 22.62
        // dyst. zm. 0.03823

        // change bill type
        solo.clickOnImageButton(0);
        assertThat((BillType) solo.getImageButton(0).getTag(MainActivity.TAG_IMAGE_TYPE),
                is(BillType.PGNIG));

        // type readings
        solo.enterText(0, "6696");
        solo.enterText(1, "7101");

        // change from date 2014.10.10
        solo.clickOnButton(0);
        solo.setDatePicker(0, 2014, 9, 10);
        solo.clickOnView(solo.getView(android.R.id.button1));
        // change to date 2014.12.15
        solo.clickOnButton(1);
        solo.setDatePicker(0, 2014, 11, 15);
        solo.clickOnView(solo.getView(android.R.id.button1));

        // calculate
        solo.clickOnButton(2);

 	    // verify na dzień 2014.10.10
        assertTrue(solo.searchText("10/10/2014"));
	    // verify na dzień 2014.12.15
        assertTrue(solo.searchText("15/12/2014"));
	    // verify odczyty
        assertTrue(solo.searchText(Pattern.quote("6696 [m\\u00B3]")));
        assertTrue(solo.searchText("7101"));
	    // verify zużycie 405 m3
        assertTrue(solo.searchText(""));
	    // verify współ.konw
        // verify ilość 4493 kWh
        // verify ilość 2 m-c
        // verify cenny netto
        // verify warotść netto 530.85 13,94 45,24 171,77
	    // verify VAT 122,10 3,21 10,41 39,51
	    // verify wartość brutto 652,95 17,15 55,65 211,28
	    // verify Razem 761,80 175,23 937,03
    }
}
