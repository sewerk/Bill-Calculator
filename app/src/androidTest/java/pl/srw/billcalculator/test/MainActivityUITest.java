package pl.srw.billcalculator.test;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

import com.robotium.solo.Solo;

import org.threeten.bp.Month;

import pl.srw.billcalculator.form.MainActivity;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.intent.IntentCreator;
import pl.srw.billcalculator.settings.GeneralPreferences;
import pl.srw.billcalculator.testutils.PreferenceUtil;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static pl.srw.billcalculator.testutils.SoloHelper.*;

/**
 * Created by Kamil Seweryn.
 */
public class MainActivityUITest extends ActivityInstrumentationTestCase2<MainActivity> {

    public MainActivityUITest() {
        super(MainActivity.class);
    }

    private Solo solo;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        GeneralPreferences.markFirstLaunch();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        PreferenceUtil.changeToG11Tariff(solo.getCurrentActivity());
        solo.finishOpenedActivities();
        super.tearDown();
    }

    public void test_shouldSwitchFormOnSwitchButtonClick() {
        assertTrue(isPgeForm(solo));

        switchBill(solo);
        assertTrue(isPgnigForm(solo));

        switchBill(solo);
        assertTrue(isPgeForm(solo));
    }

    public void test_shouldNotChangeFormOnScreenOrientationChange() {
        solo.setActivityOrientation(Solo.PORTRAIT);
        switchBill(solo);

        solo.setActivityOrientation(Solo.LANDSCAPE);
        assertTrue(isPgnigForm(solo));
    }

    public void test_shouldShowReadingHintsAccordingToBillType() {
        PreferenceUtil.changeToG11Tariff(solo.getCurrentActivity());
        solo.setActivityOrientation(Solo.PORTRAIT);
        switchBill(solo);

        assertTrue(isPgnigForm(solo));
        assertEquals(getString(solo, R.string.reading_hint_m3), findET(solo, R.id.et_reading_from).getHint());
        assertEquals(getString(solo, R.string.reading_hint_m3), findET(solo, R.id.et_reading_to).getHint());

        solo.setActivityOrientation(Solo.LANDSCAPE);
        assertEquals(getString(solo, R.string.reading_hint_m3), findET(solo, R.id.et_reading_from).getHint());
        assertEquals(getString(solo, R.string.reading_hint_m3), findET(solo, R.id.et_reading_to).getHint());

        switchBill(solo);
        assertTrue(isPgeForm(solo));
        assertEquals(getString(solo, R.string.reading_hint_kWh), findET(solo, R.id.et_reading_from).getHint());
        assertEquals(getString(solo, R.string.reading_hint_kWh), findET(solo, R.id.et_reading_to).getHint());

    }

    public void test_shouldRestoreStateOnOrientationChange() throws Throwable {
        solo.setActivityOrientation(Solo.PORTRAIT);
        final String readingFromValue = "234";
        final String readingToValue = "345";
        final String dateFromValue = "01/01/2014";
        final String dateToValue = "31/12/2014";

        switchBill(solo);
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
        assertTrue(isPgnigForm(solo));
        assertEquals(readingFromValue, solo.getEditText(0).getText().toString());
        assertEquals(readingToValue, solo.getEditText(1).getText().toString());
        assertEquals(dateFromValue, solo.getButton(0).getText());
        assertEquals(dateToValue, solo.getButton(1).getText());
    }

    public void test_shouldShowTariffLabelForPgeOnly() {
        switchBill(solo);
        assertTrue(isPgnigForm(solo));
        assertFalse(solo.searchText(getString(solo, R.string.tariff_G11_on_bill)));

        switchBill(solo);
        assertTrue(isPgeForm(solo));
        assertNotNull(solo.getView(R.id.ll_tariff));
        assertTrue(solo.searchText(getString(solo, R.string.tariff_G11_on_bill)));
    }

    public void test_shouldShowTariffLabelAccordingToPreferences() {
        // given: G11 form
        // then: G11 tariff shown
        assertTrue(solo.searchText(getString(solo, R.string.tariff_G11_on_bill)));

        // when: switch to G12 in preferences
        PreferenceUtil.changeToG12Tariff(solo.getCurrentActivity());
        redrawActivity(solo);

        // then: G12 tariff shown
        assertTrue(solo.searchText(getString(solo, R.string.tariff_G12_on_bill)));
    }

    public void test_shouldChangeFocusOnSoftNextKeyForG11() throws Throwable {
        // given: focus on first edit text
        solo.clickOnEditText(0);

        // when: soft next button clicked
        pressSoftKeyboardNextButton(solo);

        // then: focus on second edit text
        assertFalse(solo.getEditText(0).hasFocus());
        assertTrue(solo.getEditText(1).hasFocus());
    }

    public void test_shouldChangeFocusOnSoftNextKeyForG12() throws Throwable {
        // given: pge G12 form
        PreferenceUtil.changeToG12Tariff(solo.getCurrentActivity());
        redrawActivity(solo);
        // and focus on first edit text
        solo.clickOnEditText(0);

        // given: soft next button clicked
        // then: focus on other edit text
        pressSoftKeyboardNextButton(solo);
        assertTrue(solo.getEditText(2).hasFocus());
        pressSoftKeyboardNextButton(solo);
        assertTrue(solo.getEditText(1).hasFocus());
        pressSoftKeyboardNextButton(solo);
        assertTrue(solo.getEditText(3).hasFocus());
    }

    public void test_shouldShowErrorOnEmptyReadingsG11() {
        // given: empty readings
        // when: click calculate
        solo.clickOnButton(getString(solo, R.string.calculate));

        // then: error show on first et
        assertThat(solo.getEditText(0).getError().toString(), is(getString(solo, R.string.reading_missing)));

        // when: enter value to first et
        solo.enterText(0, "123");
        // and click calculate
        solo.clickOnButton(getString(solo, R.string.calculate));

        // then: error show on second et
        assertThat(solo.getEditText(1).getError().toString(), is(getString(solo, R.string.reading_missing)));
    }

    public void test_shouldShowErrorOnEmptyReadingsG12() {
        // given: empty readings G12
        PreferenceUtil.changeToG12Tariff(solo.getCurrentActivity());
        redrawActivity(solo);

        // when: click calculate
        // then: error show on et
        solo.clickOnButton(getString(solo, R.string.calculate));
        assertThat(solo.getEditText(0).getError().toString(), is(getString(solo, R.string.reading_missing)));

        solo.enterText(0, "123");
        solo.clickOnButton(getString(solo, R.string.calculate));
        assertThat(solo.getEditText(2).getError().toString(), is(getString(solo, R.string.reading_missing)));

        solo.enterText(2, "124");
        solo.clickOnButton(getString(solo, R.string.calculate));
        assertThat(solo.getEditText(1).getError().toString(), is(getString(solo, R.string.reading_missing)));

        solo.enterText(1, "123");
        solo.clickOnButton(getString(solo, R.string.calculate));
        assertThat(solo.getEditText(3).getError().toString(), is(getString(solo, R.string.reading_missing)));
    }

    public void test_shouldShowErrorOnReadingG11WrongOrder() {
        // given: G11 readings fill in wrong order
        solo.enterText(0, "123");
        solo.enterText(1, "122");

        // when: calculate clicked
        solo.clickOnButton(getString(solo, R.string.calculate));

        // then: error is shown on et
        assertTrue(solo.searchText(getString(solo, R.string.reading_order_error)));
    }

    public void test_shouldShowErrorOnReadingG12WrongOrder() {
        // given: G12 readings fill in wrong order
        PreferenceUtil.changeToG12Tariff(solo.getCurrentActivity());
        redrawActivity(solo);
        solo.enterText(0, "123");
        solo.enterText(2, "122");
        solo.enterText(1, "234");
        solo.enterText(3, "233");

        // when: calculate clicked
        // then: error shown on et
        solo.clickOnButton(getString(solo, R.string.calculate));
        assertThat(solo.getEditText(2).getError().toString(), is(getString(solo, R.string.reading_order_error)));

        solo.enterText(2, "124");
        solo.clickOnButton(getString(solo, R.string.calculate));
        assertThat(solo.getEditText(3).getError().toString(), is(getString(solo, R.string.reading_order_error)));

    }

    public void test_shouldShowErrorOnDatesWrongOrder() {
        // given: correctly set readings
        solo.enterText(0, "123");
        solo.enterText(1, "234");
        // and 'date to' set to earlier then 'date from'
        setDateOnButton(solo, 0, 2015, Month.MARCH, 15);
        setDateOnButton(solo, 1, 2015, Month.MARCH, 14);

        // when: calculate button pressed
        solo.clickOnButton(getString(solo, R.string.calculate));

        // then: error show up
        assertTrue(solo.searchText(getString(solo, R.string.date_error)));
    }

    public void test_shouldShowEnergyBillWhenEnergyBillType() {
        // given: energy form filled correctly
        solo.enterText(0, "123");
        solo.enterText(1, "234");

        // when: calculate clicked
        solo.clickOnButton(getString(solo, R.string.calculate));

        // then: energy bill opened
        assertTrue(solo.searchText(getString(solo, R.string.energy_bill)));
    }

    public void test_shouldShowGasBillWhenGasBillType() {
        // given: gas form filled correctly
        switchBill(solo);
        solo.enterText(0, "123");
        solo.enterText(1, "234");

        // when: calculate clicked
        solo.clickOnButton(getString(solo, R.string.calculate));

        // then: gas bill opened
        assertTrue(solo.searchText(getString(solo, R.string.gas_bill)));
    }

    public void test_shouldPutInputValuesToIntentForG11Form() {
        // given: G11 form filled correctly
        solo.enterText(0, "123");
        solo.enterText(1, "234");
        setDateOnButton(solo, 0, 2015, Month.MARCH, 1);
        setDateOnButton(solo, 1, 2015, Month.MARCH, 31);

        // when: calculate clicked
        solo.clickOnButton(getString(solo, R.string.calculate));
        getInstrumentation().waitForIdleSync();

        // then: next activity intent contains input values
        final Intent intent = solo.getCurrentActivity().getIntent();
        assertThat(intent.getIntExtra(IntentCreator.READING_FROM, -1), is(123));
        assertThat(intent.getIntExtra(IntentCreator.READING_TO, -1), is(234));
        assertThat(intent.getStringExtra(IntentCreator.DATE_FROM), is("01/03/2015"));
        assertThat(intent.getStringExtra(IntentCreator.DATE_TO), is("31/03/2015"));
    }

    public void test_shouldPutInputValuesToIntentForG12Form() {
        // given: G12 form filled correctly
        PreferenceUtil.changeToG12Tariff(solo.getCurrentActivity());
        redrawActivity(solo);
        solo.enterText(0, "123");
        solo.enterText(2, "234");
        solo.enterText(1, "233");
        solo.enterText(3, "456");
        setDateOnButton(solo, 0, 2015, Month.MARCH, 1);
        setDateOnButton(solo, 1, 2015, Month.MARCH, 31);

        // when: calculate clicked
        solo.clickOnButton(getString(solo, R.string.calculate));
        getInstrumentation().waitForIdleSync();

        // then: next activity intent contains input values
        final Intent intent = solo.getCurrentActivity().getIntent();
        assertThat(intent.getIntExtra(IntentCreator.READING_DAY_FROM, -1), is(123));
        assertThat(intent.getIntExtra(IntentCreator.READING_DAY_TO, -1), is(234));
        assertThat(intent.getIntExtra(IntentCreator.READING_NIGHT_FROM, -1), is(233));
        assertThat(intent.getIntExtra(IntentCreator.READING_NIGHT_TO, -1), is(456));
        assertThat(intent.getStringExtra(IntentCreator.DATE_FROM), is("01/03/2015"));
        assertThat(intent.getStringExtra(IntentCreator.DATE_TO), is("31/03/2015"));
    }

    public void test_shouldShowCheckPricesDialogOnFirstStart() {
        // given: first start
        PreferenceUtil.clearFirstLaunch(solo.getCurrentActivity());

        // when: application start
        solo.finishOpenedActivities();
        launchActivity(getActivity().getPackageName(), MainActivity.class, null);

        // then: check prices dialog show up
        solo.waitForDialogToOpen();
        assertTrue(solo.searchText(getString(solo, R.string.check_prices_info_title)));
    }
}
