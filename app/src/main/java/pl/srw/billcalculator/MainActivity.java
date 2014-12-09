package pl.srw.billcalculator;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import pl.srw.billcalculator.util.Dates;

/**
 * Created by Kamil Seweryn
 */
public class MainActivity extends Activity {

    public static final int IMAGE_TYPE_KEY = 1109171014;
    public static final String IMAGE_TYPE_STRING = "" + IMAGE_TYPE_KEY;

    public static final String READING_FROM = "READING_FROM";
    public static final String READING_TO = "READING_TO";
    public static final String DATE_FROM = "DATE_FROM";
    public static final String DATE_TO = "DATE_TO";
    public static final String SHARED_PREFERENCES_FILE = "PreferencesFile";
    public static final String PREFERENCE_KEY_FIRST_LAUNCH = "first_launch";

    @InjectView(R.id.button_bill_type_switch) ImageButton bBillType;
    @InjectView(R.id.editText_from) EditText etPreviousReading;
    @InjectView(R.id.editText_to) EditText etCurrentReading;
    @InjectView(R.id.button_date_from) Button bFromDate;
    @InjectView(R.id.button_date_to) Button bToDate;
    @InjectView(R.id.editText_date_to_error) TextView etToDateError;
    private CheckPricesDialogFragment checkPricesDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        initValues(savedInstanceState);
    }

    private void initValues(final Bundle savedInstanceState) {
        initDates();
        initBillType(savedInstanceState);
    }

    private void initDates() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);

        bFromDate.setText(Dates.format(year, month, 1));
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.add(Calendar.MONTH, 1);
        c.add(Calendar.DAY_OF_MONTH, -1);
        bToDate.setText(Dates.format(year, month, c.get(Calendar.DAY_OF_MONTH)));
    }

    private void initBillType(final Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            setBillType(restoreBillTypeFrom(savedInstanceState));
        } else {
            setBillType(BillType.PGE);
        }
    }

    private BillType restoreBillTypeFrom(final Bundle state) {
        return (BillType) state.getSerializable(IMAGE_TYPE_STRING);
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(IMAGE_TYPE_STRING, getBillType());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFirstLaunch()) {
            showCheckPricesDialog();
        }
    }

    private boolean isFirstLaunch() {
        return getSharedPreferences(SHARED_PREFERENCES_FILE, MODE_PRIVATE)
                .getString(PREFERENCE_KEY_FIRST_LAUNCH, "").isEmpty();
    }

    private void showCheckPricesDialog() {
        if (checkPricesDialog == null) {
            checkPricesDialog = new CheckPricesDialogFragment();
        }
        checkPricesDialog.show(getFragmentManager(), PREFERENCE_KEY_FIRST_LAUNCH);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (checkPricesDialog != null) {
            checkPricesDialog.dismiss();
        }
    }

    @OnClick(R.id.button_bill_type_switch)
    public void changeBillType() {
        if (getBillType() == BillType.PGE) {
            setBillType(BillType.PGNIG);
        } else {
            setBillType(BillType.PGE);
        }
    }

    private BillType getBillType() {
        return (BillType) bBillType.getTag(IMAGE_TYPE_KEY);
    }

    private void setBillType(BillType type) {
        bBillType.setBackgroundResource(type.drawableId);
        bBillType.setTag(IMAGE_TYPE_KEY, type);
        YoYo.with(Techniques.BounceIn)
                .duration(400)
                .playOn(bBillType);
    }

    @OnClick({R.id.button_date_from, R.id.button_date_to})
    public void showDatePicker(final Button datePickerButton) {
        final Calendar c = Calendar.getInstance();
        c.setTime(readDateFrom(datePickerButton));
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(MainActivity.this, onDatePickedListener(datePickerButton), year, month, day).show();
    }

    private DatePickerDialog.OnDateSetListener onDatePickedListener(final Button button) {
        return new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                button.setText(Dates.format(year, month, day));
                etToDateError.setError(null);
            }
        };
    }

    private Date readDateFrom(final Button button) {
        return Dates.parse(button.getText().toString());
    }

    @OnClick(R.id.button_calculate)
    public void calculate() {
        if (!validateForm())
            return;

        Intent billResult = newIntentBy(bBillType);
        fillParameters(billResult);
        startActivity(billResult);
    }

    private boolean validateForm() {
        return valideteReadings() && validateDates();
    }

    private boolean valideteReadings() {
        String prev = etPreviousReading.getText().toString();
        if (prev.isEmpty()) {
            shake(etPreviousReading);
            showError(etPreviousReading, R.string.reading_missing);
            return false;
        }
        String current = etCurrentReading.getText().toString();
        if (current.isEmpty()) {
            shake(etCurrentReading);
            showError(etCurrentReading, R.string.reading_missing);
            return false;
        }
        if (!(Integer.parseInt(current) > Integer.parseInt(prev))) {
            shake(etCurrentReading);
            showError(etCurrentReading, R.string.reading_error);
            return false;
        }
        return true;
    }

    private void showError(EditText et, int error_id) {
        et.setError(getString(error_id));
        et.requestFocus();
        showKeyboard(et);
    }

    private boolean validateDates() {
        String fromDate = bFromDate.getText().toString();
        String toDate = bToDate.getText().toString();
        Date from = Dates.parse(fromDate);
        Date to = Dates.parse(toDate);
        if (!from.before(to)) {
            shake(bToDate);
            etToDateError.requestFocus();
            etToDateError.setError(getString(R.string.date_error));
            return false;
        }
        return true;
    }

    private void shake(View target) {
        YoYo.with(Techniques.Shake).playOn(target);
    }

    private void showKeyboard(TextView mTextView) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            // only will trigger it if no physical keyboard is open
            imm.showSoftInput(mTextView, 0);
        }
    }

    private Intent newIntentBy(ImageButton bBillType) {
        if (getBillType() == BillType.PGE) {
            return new Intent(this, EnergyBillActivity.class);
        } else {
            return new Intent(this, GasBillActivity.class);
        }
    }

    private void fillParameters(Intent billResult) {
        String prev = etPreviousReading.getText().toString();
        billResult.putExtra(READING_FROM, Integer.parseInt(prev));
        String current = etCurrentReading.getText().toString();
        billResult.putExtra(READING_TO, Integer.parseInt(current));

        String fromDate = bFromDate.getText().toString();
        billResult.putExtra(DATE_FROM, fromDate);
        String toDate = bToDate.getText().toString();
        billResult.putExtra(DATE_TO, toDate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startSettings();
            return true;
        } else if (item.getItemId() == R.id.action_about) {
            startAbout();
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right);
    }

    private void startAbout() {
        startActivity(new Intent(this, AboutActivity.class));
    }

    public void startSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

}
