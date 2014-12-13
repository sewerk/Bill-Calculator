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
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import pl.srw.billcalculator.BillType;
import pl.srw.billcalculator.EnergyBillActivity;
import pl.srw.billcalculator.GasBillActivity;
import pl.srw.billcalculator.MainActivity;
import pl.srw.billcalculator.R;

/**
 * Created by Kamil Seweryn.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Activity sut;
    private Instrumentation mInstrumentation;
    private SharedPreferences.Editor mPricePreferences;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mInstrumentation = super.getInstrumentation();
        sut = getActivity();
        mPricePreferences = PreferenceManager.getDefaultSharedPreferences(sut).edit();
    }

    @Override
    protected void tearDown() throws Exception {
        mPricePreferences.putBoolean(sut.getString(R.string.preferences_taryfa_dwustrefowa), false).commit();
        super.tearDown();
    }

    public void testCheckPricesDialogShowUpOnce() {
        //clear preferences to simulate first access
        clearPreferences();
        //first access
        restartActivity();

        //check dialog show up
        final DialogFragment checkPricesDialog = (DialogFragment) sut.getFragmentManager().findFragmentByTag(MainActivity.PREFERENCE_KEY_FIRST_LAUNCH);
        assertTrue(checkPricesDialog.getDialog().isShowing());

        //close dialog to not show up any more
        sendKeys(KeyEvent.KEYCODE_BACK);

        //check dialog do not show up on next launch
        restartActivity();
        assertNull(sut.getFragmentManager().findFragmentByTag(MainActivity.PREFERENCE_KEY_FIRST_LAUNCH));
    }

    public void testBillTypeSwitchIsInitialized() {
        Object initialSwitchBtnTagValue = findSwitchBillTypeButtonView().getTag(MainActivity.IMAGE_TYPE_KEY);
        assertNotNull(initialSwitchBtnTagValue);
    }

    @UiThreadTest
    public void testG12InfluenceOnlyPGEReadingsView() throws Throwable {
        mPricePreferences.putBoolean(sut.getString(R.string.preferences_taryfa_dwustrefowa), true).commit();
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
        //change state
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
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

    @UiThreadTest
    public void testReadingValidationOnCalculate() {
        final String readingLess = "234";
        final String readingMore = "345";

        // validate empty edit text
        findCalculateView().performClick();
        assertEquals(sut.getString(R.string.reading_missing), findReadingFromView().getError());

        //input values in incorrect order
        findReadingFromView().setText(readingMore);
        findReadingToView().setText(readingLess);

        //validate values order
        findCalculateView().performClick();
        assertEquals(sut.getString(R.string.reading_error), findReadingToView().getError());
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

    public void testBillTypeSwitchChooseNextScreen() throws Throwable {
        final String readingLess = "234";
        final String readingMore = "345";

        Instrumentation.ActivityMonitor pgeBillMonitor = getInstrumentation().addMonitor(EnergyBillActivity.class.getName(), null, false);
        Instrumentation.ActivityMonitor pgnigBillMonitor = getInstrumentation().addMonitor(GasBillActivity.class.getName(), null, false);

        // input values PGNIG and calculate
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                findSwitchBillTypeButtonView().setTag(MainActivity.IMAGE_TYPE_KEY, BillType.PGNIG);
                findReadingFromView().setText(readingLess);
                findReadingToView().setText(readingMore);

                findCalculateView().performClick();
            }
        });

        //validate PGNIG bill show up
        Activity billActivity = getInstrumentation().waitForMonitorWithTimeout(pgnigBillMonitor, 5000L);
        billActivity.finish();
        assertEquals(GasBillActivity.class, billActivity.getClass());

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
        assertEquals(EnergyBillActivity.class, billActivity.getClass());
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

    private EditText findReadingToView() {
        return (EditText) sut.findViewById(R.id.editText_reading_to);
    }

    private EditText findReadingFromView() {
        return (EditText) sut.findViewById(R.id.editText_reading_from);
    }

    private LinearLayout findReadingsG11View() {
        return (LinearLayout) sut.findViewById(R.id.linearLayout_reading_from_to);
    }

    private TableLayout findReadingsG12View() {
        return (TableLayout) sut.findViewById(R.id.tableLayout_readings);
    }

    private ImageButton findSwitchBillTypeButtonView() {
        return (ImageButton) sut.findViewById(R.id.button_bill_type_switch);
    }

    public Instrumentation getInstrumentation() {
        return mInstrumentation;
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
    private void restartActivity() {
        sut.finish();
        setActivity(null);
        sut = getActivity();
    }

    private void clearPreferences() {
        SharedPreferences.Editor editor = sut.getSharedPreferences(MainActivity.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }


}
