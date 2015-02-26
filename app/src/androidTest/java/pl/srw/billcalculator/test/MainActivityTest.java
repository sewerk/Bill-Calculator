package pl.srw.billcalculator.test;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Instrumentation;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.Date;

import pl.srw.billcalculator.PgeBillActivity;
import pl.srw.billcalculator.PgnigBillActivity;
import pl.srw.billcalculator.intent.BillActivityIntentFactory;
import pl.srw.billcalculator.type.BillType;
import pl.srw.billcalculator.MainActivity;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.preference.PgeSettingsFragment;

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
        changeToG11Tariff();
        markAfterFirstLaunch();
        sut = getActivity();
    }

    public void testCheckPricesDialogShowUpOnce() throws InterruptedException {
        //clear preferences to simulate first access
        clearPreferences();
        //first access
        restartActivity();

        assertTrue(getFirstLaunchPreferenceValue().isEmpty());

        getInstrumentation().waitForIdleSync();
        Thread.sleep(1000L);//need to make sure the fragment dialog show up
        //check dialog show up
        final DialogFragment checkPricesDialog =
                (DialogFragment) sut.getFragmentManager().findFragmentByTag(MainActivity.PREFERENCE_KEY_FIRST_LAUNCH);
        assertTrue(checkPricesDialog.getDialog().isShowing());

        //close dialog to not show up any more
        Log.d("testCheckPricesDialogShowUpOnce", "===== back button send");
        sendKeys(KeyEvent.KEYCODE_BACK);

        //check dialog do not show up on next launch
        restartActivity();

        assertFalse(getFirstLaunchPreferenceValue().isEmpty());

        getInstrumentation().waitForIdleSync();
        assertNull(sut.getFragmentManager().findFragmentByTag(MainActivity.PREFERENCE_KEY_FIRST_LAUNCH));
    }

    public void testBillTypeSwitchIsInitialized() {
        Object initialSwitchBtnTagValue = findSwitchBillTypeButtonView().getTag(MainActivity.IMAGE_TYPE_KEY);
        assertNotNull(initialSwitchBtnTagValue);
    }

    @UiThreadTest
    public void testG12InfluenceOnlyPGEReadingsView() throws Throwable {
        changeToG12Tariff();
        //switch to PGNiG
        findSwitchBillTypeButtonView().performClick();
        assertEquals(View.VISIBLE, findReadingsG11View().getVisibility());
        assertEquals(View.GONE, findReadingsG12View().getVisibility());

        //switch to PGE
        findSwitchBillTypeButtonView().performClick();
        assertEquals(View.GONE, findReadingsG11View().getVisibility());
        assertEquals(View.VISIBLE, findReadingsG12View().getVisibility());
    }
    
    @UiThreadTest
    public void testBillTypeSwitchOnClick() {
        ImageButton switchBtn = findSwitchBillTypeButtonView();
        Object initialSwitchBtnTagValue = switchBtn.getTag(MainActivity.IMAGE_TYPE_KEY);

        switchBtn.performClick();
        assertNotSame(initialSwitchBtnTagValue, switchBtn.getTag(MainActivity.IMAGE_TYPE_KEY));

        switchBtn.performClick();
        assertSame(initialSwitchBtnTagValue, switchBtn.getTag(MainActivity.IMAGE_TYPE_KEY));
    }

    public void testRestoreState() throws Throwable {
        final BillType billTypeValue = BillType.PGNIG;
        final String readingFromValue = "234";
        final String readingToValue = "345";
        final String dateFromValue = "01/01/2014";
        final String dateToValue = "31/12/2014";

        final Bundle bundle = new Bundle();
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                //change state
                findSwitchBillTypeButtonView().setTag(MainActivity.IMAGE_TYPE_KEY, billTypeValue);
                findReadingFromView().setText(readingFromValue);
                findReadingToView().setText(readingToValue);
                findDateFromView().setText(dateFromValue);
                findDateToView().setText(dateToValue);
                //save state
                getInstrumentation().callActivityOnSaveInstanceState(sut, bundle);
            }
        });

        restartActivity();
        //restore state
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                getInstrumentation().callActivityOnRestoreInstanceState(sut, bundle);
            }
        });

        assertEquals(billTypeValue, findSwitchBillTypeButtonView().getTag(MainActivity.IMAGE_TYPE_KEY));
        assertEquals(readingFromValue, findReadingFromView().getText().toString());
        assertEquals(readingToValue, findReadingToView().getText().toString());
        assertEquals(dateFromValue, findDateFromView().getText());
        assertEquals(dateToValue, findDateToView().getText());
    }

    public void testScreenOrientationChangeDoesNotChangeReadingsLayout() throws Throwable {
        changeToG12Tariff();

        final Bundle bundle = new Bundle();
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                //change state
                findSwitchBillTypeButtonView().performClick();
                //save state
                getInstrumentation().callActivityOnSaveInstanceState(sut, bundle);
            }
        });

        restartActivity();
        //restore state
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                getInstrumentation().callActivityOnRestoreInstanceState(sut, bundle);
            }
        });

        assertEquals(BillType.PGNIG, findSwitchBillTypeButtonView().getTag(MainActivity.IMAGE_TYPE_KEY));
        assertEquals(View.VISIBLE, findReadingsG11View().getVisibility());
        assertEquals(View.GONE, findReadingsG12View().getVisibility());
    }

    public void testReadingHintsShowAccordingToBillType() throws Throwable {
        final Bundle bundle = new Bundle();
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                findSwitchBillTypeButtonView().performClick();
                assertEquals(BillType.PGNIG, findSwitchBillTypeButtonView().getTag(MainActivity.IMAGE_TYPE_KEY));
                assertEquals(sut.getString(R.string.reading_hint_m3), findReadingFromView().getHint());
                assertEquals(sut.getString(R.string.reading_hint_m3), findReadingToView().getHint());

                //save state
                getInstrumentation().callActivityOnSaveInstanceState(sut, bundle);
            }
        });

        restartActivity();
        // restore state
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                getInstrumentation().callActivityOnRestoreInstanceState(sut, bundle);

                assertEquals(BillType.PGNIG, findSwitchBillTypeButtonView().getTag(MainActivity.IMAGE_TYPE_KEY));
                assertEquals(sut.getString(R.string.reading_hint_m3), findReadingFromView().getHint());
                assertEquals(sut.getString(R.string.reading_hint_m3), findReadingToView().getHint());

                findSwitchBillTypeButtonView().performClick();
                assertEquals(BillType.PGE, findSwitchBillTypeButtonView().getTag(MainActivity.IMAGE_TYPE_KEY));
                assertEquals(sut.getString(R.string.reading_hint_kWh), findReadingFromView().getHint());
                assertEquals(sut.getString(R.string.reading_hint_kWh), findReadingToView().getHint());
            }
        });
    }

    public void testTariffLabelShowUpForPGE() throws Throwable {
        final Bundle bundle = new Bundle();
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                findSwitchBillTypeButtonView().performClick();
                assertEquals(BillType.PGNIG, findSwitchBillTypeButtonView().getTag(MainActivity.IMAGE_TYPE_KEY));
                assertEquals(View.INVISIBLE, findTariffView().getVisibility());

                //save state
                getInstrumentation().callActivityOnSaveInstanceState(sut, bundle);
            }
        });

        restartActivity();
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                // restore state
                getInstrumentation().callActivityOnRestoreInstanceState(sut, bundle);
                assertEquals(View.INVISIBLE, findTariffView().getVisibility());

                findSwitchBillTypeButtonView().performClick();
                assertEquals(BillType.PGE, findSwitchBillTypeButtonView().getTag(MainActivity.IMAGE_TYPE_KEY));
                assertEquals(View.VISIBLE, findTariffView().getVisibility());
            }
        });

    }

    public void testG12InfluenceTariffLabel() {
        assertEquals(sut.getString(R.string.pge_tariff_G11_on_bill), findTariffLabelView().getText().toString());

        changeToG12Tariff();
        restartActivity();

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
        changeToG12Tariff();
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

    public void testInputValues() throws Throwable {
        requestFocus(findReadingFromView());
        sendKeys("2 3 4");
        requestFocus(findReadingToView());
        sendKeys("3 4 5");

        assertEquals("234", findReadingFromView().getText().toString());
        assertEquals("345", findReadingToView().getText().toString());
    }

    public void testFocusChangeForG11Readings() throws Throwable {
        requestFocus(findReadingFromView());
        sendKeys("1");
        runImeActionOn(findReadingFromView());

        assertFalse(findReadingFromView().hasFocus());
        assertTrue(findReadingToView().hasFocus());
    }

    public void testFocusChangeForG12Readings() throws Throwable {
        changeToG12Tariff();
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
        changeToG12Tariff();
        restartActivity();

        requestFocus(findReadingDayFromView());
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                findSwitchBillTypeButtonView().performClick();
                assertTrue(findReadingFromView().hasFocus());

                findSwitchBillTypeButtonView().performClick();
                assertFalse(findReadingFromView().hasFocus());
                assertTrue(findReadingDayFromView().hasFocus());
            }
        });
    }

    public void testBillTypeSwitchChooseNextScreen() throws Throwable {
        final int readingLess = 234;
        final int readingMore = 345;

        Instrumentation.ActivityMonitor pgeBillMonitor = getInstrumentation().addMonitor(PgeBillActivity.class.getName(), null, false);
        Instrumentation.ActivityMonitor pgnigBillMonitor = getInstrumentation().addMonitor(PgnigBillActivity.class.getName(), null, false);

        // input values PGNIG and calculate
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                findSwitchBillTypeButtonView().setTag(MainActivity.IMAGE_TYPE_KEY, BillType.PGNIG);
                findReadingFromView().setText("" + readingLess);
                findReadingToView().setText(""+readingMore);

                findCalculateView().performClick();
            }
        });

        //validate PGNIG bill show up
        Activity billActivity = getInstrumentation().waitForMonitorWithTimeout(pgnigBillMonitor, 5000L);
        billActivity.finish();
        assertEquals(readingLess, billActivity.getIntent().getIntExtra(BillActivityIntentFactory.READING_FROM, -1));
        assertEquals(readingMore, billActivity.getIntent().getIntExtra(BillActivityIntentFactory.READING_TO, -1));
        assertEquals(PgnigBillActivity.class, billActivity.getClass());
        getInstrumentation().removeMonitor(pgnigBillMonitor);

        //change bill type PGE and calculate
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                findSwitchBillTypeButtonView().setTag(MainActivity.IMAGE_TYPE_KEY, BillType.PGE);

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
        changeToG12Tariff();
        restartActivity();

        // input mandatory values
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                findReadingDayFromView().setText(""+dayFrom);
                findReadingDayToView().setText(""+dayTo);
                findReadingNightFromView().setText(""+nightFrom);
                findReadingNightToView().setText(""+nightTo);

                findCalculateView().performClick();
            }
        });

        //validate PGNIG bill show up
        Instrumentation.ActivityMonitor pgeBillMonitor =
                getInstrumentation().addMonitor(PgeBillActivity.class.getName(), null, false);
        Activity billActivity = getInstrumentation().waitForMonitorWithTimeout(pgeBillMonitor, 5000L);
        billActivity.finish();
        
        assertEquals(dayFrom, billActivity.getIntent().getIntExtra(BillActivityIntentFactory.READING_DAY_FROM, -1));
        assertEquals(nightFrom, billActivity.getIntent().getIntExtra(BillActivityIntentFactory.READING_NIGHT_FROM, -1));
        assertEquals(dayTo, billActivity.getIntent().getIntExtra(BillActivityIntentFactory.READING_DAY_TO, -1));
        assertEquals(nightTo, billActivity.getIntent().getIntExtra(BillActivityIntentFactory.READING_NIGHT_TO, -1));
        getInstrumentation().removeMonitor(pgeBillMonitor);
    }

    // ================================================================ private methods
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
        return (EditText) sut.findViewById(R.id.editText_reading_from);
    }

    private EditText findReadingToView() {
        return (EditText) sut.findViewById(R.id.editText_reading_to);
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
    
    private ImageButton findSwitchBillTypeButtonView() {
        return (ImageButton) sut.findViewById(R.id.button_bill_type_switch);
    }

    private void requestFocus(final View view) throws Throwable {
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

    private void clearPreferences() {
        SharedPreferences.Editor editor = sut.getSharedPreferences(MainActivity.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE).edit();
        editor.clear().commit();
    }

    private void changeToG11Tariff() {
        final Context context = getInstrumentation().getTargetContext();
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit().putString(context.getString(R.string.preferences_pge_tariff), PgeSettingsFragment.TARIFF_G11)
                .commit();
    }

    private void changeToG12Tariff() {
        final Context context = getInstrumentation().getTargetContext();
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit().putString(context.getString(R.string.preferences_pge_tariff), PgeSettingsFragment.TARIFF_G12)
                .commit();
    }

    private void markAfterFirstLaunch() {
        getInstrumentation().getTargetContext().
                getSharedPreferences(MainActivity.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE)
                .edit().putString(MainActivity.PREFERENCE_KEY_FIRST_LAUNCH, new Date().toString())
                .commit();
    }

    private String getFirstLaunchPreferenceValue() {
        return sut.
                getSharedPreferences(MainActivity.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE)
                .getString(MainActivity.PREFERENCE_KEY_FIRST_LAUNCH, "");
    }
}
