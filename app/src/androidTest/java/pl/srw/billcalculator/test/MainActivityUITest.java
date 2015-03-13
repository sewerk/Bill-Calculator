package pl.srw.billcalculator.test;

import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
import android.widget.Button;
import android.widget.EditText;

import com.robotium.solo.Condition;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.srw.billcalculator.MainActivity;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.preference.GeneralPreferences;

/**
 * Created by Kamil Seweryn.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityUITest extends ActivityInstrumentationTestCase2<MainActivity> {

    public static final int TIMEOUT = 1000;

    public MainActivityUITest() {
        super(MainActivity.class);
    }

    private Solo solo;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        GeneralPreferences.markFirstLaunch();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @After
    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }

    @Test
    public void shouldSwitchFormOnSwitchButtonClick() {
        assertTrue(isPgeForm());

        solo.clickOnView(solo.getView(R.id.iv_bill_type_switch));
        solo.waitForCondition(pgeFormHidden(), TIMEOUT);
        assertTrue(isPgnigForm());

        solo.clickOnView(solo.getView(R.id.iv_bill_type_switch));
        solo.waitForCondition(pgnigFormHidden(), TIMEOUT);
        assertTrue(isPgeForm());
    }

    @Test
    public void shouldNotChangeFormOnScreenOrientationChange() {
        solo.setActivityOrientation(Solo.PORTRAIT);
        solo.clickOnView(solo.getView(R.id.iv_bill_type_switch));

        solo.setActivityOrientation(Solo.LANDSCAPE);
        assertTrue(isPgnigForm());
    }

    @Test
    public void shouldShowReadingHintsAccordingToBillType() {
        solo.setActivityOrientation(Solo.PORTRAIT);
        solo.clickOnView(solo.getView(R.id.iv_bill_type_switch));

        solo.waitForCondition(pgeFormHidden(), TIMEOUT);
        assertTrue(isPgnigForm());
        assertEquals(getString(R.string.reading_hint_m3), findET(R.id.et_reading_from).getHint());
        assertEquals(getString(R.string.reading_hint_m3), findET(R.id.et_reading_to).getHint());

        solo.setActivityOrientation(Solo.LANDSCAPE);
        assertEquals(getString(R.string.reading_hint_m3), findET(R.id.et_reading_from).getHint());
        assertEquals(getString(R.string.reading_hint_m3), findET(R.id.et_reading_to).getHint());

        solo.clickOnView(solo.getView(R.id.iv_bill_type_switch));
        solo.waitForCondition(pgnigFormHidden(), TIMEOUT);
        assertEquals(getString(R.string.reading_hint_kWh), findET(R.id.et_reading_from).getHint());
        assertEquals(getString(R.string.reading_hint_kWh), findET(R.id.et_reading_to).getHint());

    }

    @Test
    public void shouldRestoreStateOnOrientationChange() throws Throwable {
        solo.setActivityOrientation(Solo.PORTRAIT);
        final String readingFromValue = "234";
        final String readingToValue = "345";
        final String dateFromValue = "01/01/2014";
        final String dateToValue = "31/12/2014";

        solo.clickOnView(solo.getView(R.id.iv_bill_type_switch));
        solo.enterText(0, readingFromValue);
        solo.enterText(1, readingToValue);
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((Button) solo.getView(R.id.button_date_from)).setText(dateFromValue);
                ((Button) solo.getView(R.id.button_date_to)).setText(dateToValue);
            }
        });

        solo.setActivityOrientation(Solo.LANDSCAPE);
        assertTrue(isPgnigForm());
        assertEquals(readingFromValue, solo.getEditText(0).getText().toString());
        assertEquals(readingToValue, solo.getEditText(1).getText().toString());
        assertEquals(dateFromValue, solo.getButton(0).getText());
        assertEquals(dateToValue, solo.getButton(1).getText());
    }

    @Test
    public void shouldShowTariffLabelForPgeOnly() {
        solo.clickOnView(solo.getView(R.id.iv_bill_type_switch));
        assertTrue(isPgnigForm());
        assertFalse(solo.searchText(getString(R.string.pge_tariff_G11_on_bill)));

        solo.clickOnView(solo.getView(R.id.iv_bill_type_switch));
        solo.waitForCondition(pgnigFormHidden(), TIMEOUT);
        assertTrue(isPgeForm());
        assertNotNull(solo.getView(R.id.linearLayout_tariff));
        assertTrue(solo.searchText(getString(R.string.pge_tariff_G11_on_bill)));
    }

    private Condition pgeFormHidden() {
        return new Condition() {
            @Override
            public boolean isSatisfied() {
                return !isPgeForm();
            }
        };
    }

    private Condition pgnigFormHidden() {
        return new Condition() {
            @Override
            public boolean isSatisfied() {
                return !isPgnigForm();
            }
        };
    }

    private boolean isPgnigForm() {
        return isVisible(R.drawable.pgnig_on_pge);
    }

    private boolean isPgeForm() {
        return isVisible(R.drawable.pge_on_pgnig);
    }

    private boolean isVisible(@DrawableRes int drawable) {
        return solo.getCurrentActivity().getResources().getDrawable(drawable).isVisible();
    }

    private String getString(@StringRes final int strRes) {
        return getActivity().getString(strRes);
    }

    private EditText findET(@IdRes final int edId) {
        return (EditText) solo.getView(edId);
    }
}
