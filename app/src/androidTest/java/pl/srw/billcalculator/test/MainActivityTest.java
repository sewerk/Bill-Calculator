package pl.srw.billcalculator.test;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import pl.srw.billcalculator.MainActivity;
import pl.srw.billcalculator.PgeBillActivity;
import pl.srw.billcalculator.PgnigBillActivity;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.intent.IntentCreator;
import pl.srw.billcalculator.preference.GeneralPreferences;

/**
 * Created by Kamil Seweryn.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Activity sut;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        PreferenceUtil.changeToG11Tariff(getInstrumentation().getTargetContext());
        GeneralPreferences.markFirstLaunch();
        sut = getActivity();
    }

    public void testCheckPricesDialogShowUpOnce() throws InterruptedException {
        //clear preferences to simulate first access
        PreferenceUtil.clearFirstLaunch(sut);
        //first access
        restartActivity();

        assertTrue(PreferenceUtil.getFirstLaunchValue(sut).isEmpty());

        getInstrumentation().waitForIdleSync();
        Thread.sleep(1000L);//need to make sure the fragment dialog show up
        //check dialog show up
        final DialogFragment checkPricesDialog =
                (DialogFragment) sut.getFragmentManager().findFragmentByTag(MainActivity.TAG_CHECK_PRICES_DIALOG);
        assertTrue(checkPricesDialog.getDialog().isShowing());

        //close dialog to not show up any more
        Log.d("MainActivityTest", "testCheckPricesDialogShowUpOnce: back button send");
        sendKeys(KeyEvent.KEYCODE_BACK);

        //check dialog do not show up on next launch
        restartActivity();

        assertFalse(PreferenceUtil.getFirstLaunchValue(sut).isEmpty());

        getInstrumentation().waitForIdleSync();
        assertNull(sut.getFragmentManager().findFragmentByTag(MainActivity.TAG_CHECK_PRICES_DIALOG));
    }

    public void testG12InfluenceOnlyPGEReadingsView() throws Throwable {
        PreferenceUtil.changeToG12Tariff(sut);
        //switch to PGNiG
        switchBillType();
        assertEquals(View.VISIBLE, findReadingsG11View().getVisibility());
        assertNull(findReadingsG12View());

        //switch to PGE
        switchBillType();
        assertEquals(View.GONE, findReadingsG11View().getVisibility());
        assertEquals(View.VISIBLE, findReadingsG12View().getVisibility());
    }
    
    public void testG12InfluenceTariffLabel() {
        assertEquals(sut.getString(R.string.pge_tariff_G11_on_bill), findTariffLabelView().getText().toString());

        PreferenceUtil.changeToG12Tariff(sut);
        restartActivity();
        getInstrumentation().waitForIdleSync();
        assertEquals(sut.getString(R.string.pge_tariff_G12_on_bill), findTariffLabelView().getText().toString());
    }

    @UiThreadTest
    public void testReadingValidationOnCalculate() {
        final String readingLess = "234";
        final String readingMore = "345";

        // validate empty edit text
        findCalculateView().performClick();
        assertEquals(sut.getString(R.string.reading_missing), findReadingFromView().getError());

        //input value
        findReadingFromView().setText(readingMore);
        // validate empty edit text
        findCalculateView().performClick();
        assertEquals(sut.getString(R.string.reading_missing), findReadingToView().getError());

        //input values in incorrect order
        findReadingToView().setText(readingLess);
        //validate values order
        findCalculateView().performClick();
        assertEquals(sut.getString(R.string.reading_order_error), findReadingToView().getError());
    }

    public void testG12ReadingsValidationOnCalculate() throws Throwable {
        //change to G12 tariff
        PreferenceUtil.changeToG12Tariff(sut);
        restartActivity();

        // validate empty day from
        performCalculate();
        assertEquals(sut.getString(R.string.reading_missing), findReadingDayFromView().getError());
        findReadingDayFromView().setText("12");

        // validate empty day to
        performCalculate();
        assertEquals(sut.getString(R.string.reading_missing), findReadingDayToView().getError());
        findReadingDayToView().setText("11");

        // validate empty night from
        performCalculate();
        assertEquals(sut.getString(R.string.reading_missing), findReadingNightFromView().getError());
        findReadingNightFromView().setText("10");

        // validate empty night to
        performCalculate();
        assertEquals(sut.getString(R.string.reading_missing), findReadingNightToView().getError());
        findReadingNightToView().setText("9");

        // validate incorrect day readings order
        performCalculate();
        assertEquals(sut.getString(R.string.reading_order_error), findReadingDayToView().getError());
        findReadingDayToView().setText("13");

        // validate incorrect night readings order
        performCalculate();
        assertEquals(sut.getString(R.string.reading_order_error), findReadingNightToView().getError());
    }

    @UiThreadTest
    public void testDatesValidationOnCalculate() {
        final String earlier = "01/01/2014";
        final String later = "31/12/2014";
        final String readingLess = "234";
        final String readingMore = "345";

        //input correct reading values
        findReadingFromView().setText(readingLess);
        findReadingToView().setText(readingMore);
        //input dates in incorrect order
        findDateFromView().setText(later);
        findDateToView().setText(earlier);

        //validate dates order
        findCalculateView().performClick();
        assertEquals(sut.getString(R.string.date_error), findDateToErrorView().getError());
    }

    public void testFocusChangeForG11Readings() throws Throwable {
        requestFocus(findReadingFromView());
        sendKeys("1");
        runImeActionOn(findReadingFromView());

        assertFalse(findReadingFromView().hasFocus());
        assertTrue(findReadingToView().hasFocus());
    }

    public void testFocusChangeForG12Readings() throws Throwable {
        PreferenceUtil.changeToG12Tariff(sut);
        restartActivity();

        requestFocus(findReadingDayFromView());
        sendKeys("1");
        runImeActionOn(findReadingDayFromView());

        assertTrue(findReadingDayToView().hasFocus());
        sendKeys("2");
        //sendKeys(KeyEvent.KEYCODE_ENTER);
        runImeActionOn(findReadingDayToView());

        getInstrumentation().waitForIdleSync();
        assertFalse(findReadingDayToView().hasFocus());
        assertTrue(findReadingNightFromView().hasFocus());
        sendKeys("3");
        runImeActionOn(findReadingNightFromView());

        assertTrue(findReadingNightToView().hasFocus());
    }

    public void testFocusChangeOnBillTypeSwitch() throws Throwable {
        PreferenceUtil.changeToG12Tariff(sut);
        restartActivity();

        requestFocus(findReadingDayFromView());
        switchBillType();
        assertTrue(findReadingFromView().hasFocus());

        switchBillType();
        assertFalse(findReadingFromView().hasFocus());
        assertTrue(findReadingDayFromView().hasFocus());
    }

    public void testBillTypeSwitchChooseNextScreen() throws Throwable {
        final int readingLess = 234;
        final int readingMore = 345;

        Instrumentation.ActivityMonitor pgeBillMonitor = getInstrumentation().addMonitor(PgeBillActivity.class.getName(), null, false);
        Instrumentation.ActivityMonitor pgnigBillMonitor = getInstrumentation().addMonitor(PgnigBillActivity.class.getName(), null, false);

        // input values PGNIG and calculate
        switchBillType();
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                findReadingFromView().setText("" + readingLess);
                findReadingToView().setText("" + readingMore);

                findCalculateView().performClick();
            }
        });

        //validate PGNIG bill show up
        Activity billActivity = getInstrumentation().waitForMonitorWithTimeout(pgnigBillMonitor, 5000L);
        billActivity.finish();
        assertEquals(readingLess, billActivity.getIntent().getIntExtra(IntentCreator.READING_FROM, -1));
        assertEquals(readingMore, billActivity.getIntent().getIntExtra(IntentCreator.READING_TO, -1));
        assertEquals(PgnigBillActivity.class, billActivity.getClass());
        getInstrumentation().removeMonitor(pgnigBillMonitor);

        //change bill type PGE and calculate
        switchBillType();
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                findReadingFromView().setText("" + readingLess);
                findReadingToView().setText("" + readingMore);

                findCalculateView().performClick();
            }
        });

        //validate PGE bill show up
        billActivity = getInstrumentation().waitForMonitorWithTimeout(pgeBillMonitor, 5000L);
        billActivity.finish();
        assertEquals(PgeBillActivity.class, billActivity.getClass());
        getInstrumentation().removeMonitor(pgeBillMonitor);
    }

    public void testG12ReadingsPutToIntent() throws Throwable {
        final int dayFrom = 12;
        final int dayTo = 13;
        final int nightFrom = 11;
        final int nightTo = 13;
        //change to G12 tariff
        PreferenceUtil.changeToG12Tariff(sut);
        restartActivity();

        // input mandatory values
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                findReadingDayFromView().setText("" + dayFrom);
                findReadingDayToView().setText("" + dayTo);
                findReadingNightFromView().setText("" + nightFrom);
                findReadingNightToView().setText("" + nightTo);

                findCalculateView().performClick();
            }
        });

        //validate PGNIG bill show up
        Instrumentation.ActivityMonitor pgeBillMonitor =
                getInstrumentation().addMonitor(PgeBillActivity.class.getName(), null, false);
        Activity billActivity = getInstrumentation().waitForMonitorWithTimeout(pgeBillMonitor, 5000L);
        billActivity.finish();
        
        assertEquals(dayFrom, billActivity.getIntent().getIntExtra(IntentCreator.READING_DAY_FROM, -1));
        assertEquals(nightFrom, billActivity.getIntent().getIntExtra(IntentCreator.READING_NIGHT_FROM, -1));
        assertEquals(dayTo, billActivity.getIntent().getIntExtra(IntentCreator.READING_DAY_TO, -1));
        assertEquals(nightTo, billActivity.getIntent().getIntExtra(IntentCreator.READING_NIGHT_TO, -1));
        getInstrumentation().removeMonitor(pgeBillMonitor);
    }

    // ================================================================ private methods
    private Fragment findInputSectionFragment() {
        return sut.getFragmentManager().findFragmentById(R.id.fl_input_section);
    }

    private Button findDateToView() {
        return (Button) sut.findViewById(R.id.button_date_to);
    }

    private EditText findDateToErrorView() {
        return (EditText) sut.findViewById(R.id.editText_date_to_error);
    }

    private Button findCalculateView() {
        return (Button) sut.findViewById(R.id.button_calculate);
    }

    private Button findDateFromView() {
        return (Button) sut.findViewById(R.id.button_date_from);
    }

    private EditText findReadingFromView() {
        return (EditText) sut.findViewById(R.id.et_reading_from);
    }

    private EditText findReadingToView() {
        return (EditText) sut.findViewById(R.id.et_reading_to);
    }

    private EditText findReadingDayFromView() {
        return (EditText) sut.findViewById(R.id.editText_reading_day_from);
    }

    private EditText findReadingDayToView() {
        return (EditText) sut.findViewById(R.id.editText_reading_day_to);
    }

    private EditText findReadingNightFromView() {
        return (EditText) sut.findViewById(R.id.editText_reading_night_from);
    }

    private EditText findReadingNightToView() {
        return (EditText) sut.findViewById(R.id.editText_reading_night_to);
    }

    private LinearLayout findReadingsG11View() {
        return (LinearLayout) sut.findViewById(R.id.linearLayout_readings);
    }

    private TableLayout findReadingsG12View() {
        return (TableLayout) sut.findViewById(R.id.tableLayout_G12_readings);
    }
    
    private LinearLayout findTariffView() {
        return (LinearLayout) sut.findViewById(R.id.linearLayout_tariff);
    }

    private TextView findTariffLabelView() {
        return (TextView) sut.findViewById(R.id.textView_tariff);
    }
    
    private ImageView findSwitchBillTypeView() {
        return (ImageView) sut.findViewById(R.id.iv_bill_type_switch);
    }

    private void requestFocus(final View view) throws Throwable {
        if (view == null) {
            throw new NullPointerException();
        }
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.requestFocus();
            }
        });
        getInstrumentation().waitForIdleSync();
    }

    private void runImeActionOn(final EditText view) throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.onEditorAction(5);
            }
        });
    }

    private void restartActivity() {
        sut.finish();
        setActivity(null);
        sut = getActivity();
    }

    private void performCalculate() throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                findCalculateView().performClick();
            }
        });
    }

    private void switchBillType() throws Throwable {
        getInstrumentation().waitForIdleSync();
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                findSwitchBillTypeView().performClick();
            }
        });
        getInstrumentation().waitForIdleSync();
    }
}
