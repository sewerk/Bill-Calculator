package pl.srw.billcalculator.test;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
import android.widget.Button;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.srw.billcalculator.MainActivity;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.preference.GeneralPreferences;
import pl.srw.billcalculator.testutils.PreferenceUtil;
import pl.srw.billcalculator.testutils.SoloHelper;

/**
 * Created by Kamil Seweryn.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityUITest extends ActivityInstrumentationTestCase2<MainActivity> {

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
        assertTrue(SoloHelper.isPgeForm(solo));

        SoloHelper.switchBill(solo);
        assertTrue(SoloHelper.isPgnigForm(solo));

        SoloHelper.switchBill(solo);
        assertTrue(SoloHelper.isPgeForm(solo));
    }

    @Test
    public void shouldNotChangeFormOnScreenOrientationChange() {
        solo.setActivityOrientation(Solo.PORTRAIT);
        SoloHelper.switchBill(solo);

        solo.setActivityOrientation(Solo.LANDSCAPE);
        assertTrue(SoloHelper.isPgnigForm(solo));
    }

    @Test
    public void shouldShowReadingHintsAccordingToBillType() {
        PreferenceUtil.changeToG11Tariff(solo.getCurrentActivity());
        solo.setActivityOrientation(Solo.PORTRAIT);
        SoloHelper.switchBill(solo);

        assertTrue(SoloHelper.isPgnigForm(solo));
        assertEquals(SoloHelper.getString(solo, R.string.reading_hint_m3), SoloHelper.findET(solo,R.id.et_reading_from).getHint());
        assertEquals(SoloHelper.getString(solo, R.string.reading_hint_m3), SoloHelper.findET(solo,R.id.et_reading_to).getHint());

        solo.setActivityOrientation(Solo.LANDSCAPE);
        assertEquals(SoloHelper.getString(solo, R.string.reading_hint_m3), SoloHelper.findET(solo,R.id.et_reading_from).getHint());
        assertEquals(SoloHelper.getString(solo, R.string.reading_hint_m3), SoloHelper.findET(solo,R.id.et_reading_to).getHint());

        SoloHelper.switchBill(solo);
        assertEquals(SoloHelper.getString(solo, R.string.reading_hint_kWh), SoloHelper.findET(solo,R.id.et_reading_from).getHint());
        assertEquals(SoloHelper.getString(solo, R.string.reading_hint_kWh), SoloHelper.findET(solo,R.id.et_reading_to).getHint());

    }

    @Test
    public void shouldRestoreStateOnOrientationChange() throws Throwable {
        solo.setActivityOrientation(Solo.PORTRAIT);
        final String readingFromValue = "234";
        final String readingToValue = "345";
        final String dateFromValue = "01/01/2014";
        final String dateToValue = "31/12/2014";

        SoloHelper.switchBill(solo);
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
        assertTrue(SoloHelper.isPgnigForm(solo));
        assertEquals(readingFromValue, solo.getEditText(0).getText().toString());
        assertEquals(readingToValue, solo.getEditText(1).getText().toString());
        assertEquals(dateFromValue, solo.getButton(0).getText());
        assertEquals(dateToValue, solo.getButton(1).getText());
    }

    @Test
    public void shouldShowTariffLabelForPgeOnly() {
        SoloHelper.switchBill(solo);
        assertTrue(SoloHelper.isPgnigForm(solo));
        assertFalse(solo.searchText(SoloHelper.getString(solo, R.string.pge_tariff_G11_on_bill)));

        SoloHelper.switchBill(solo);
        assertTrue(SoloHelper.isPgeForm(solo));
        assertNotNull(solo.getView(R.id.linearLayout_tariff));
        assertTrue(solo.searchText(SoloHelper.getString(solo, R.string.pge_tariff_G11_on_bill)));
    }
}
