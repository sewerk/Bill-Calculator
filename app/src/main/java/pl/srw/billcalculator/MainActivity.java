package pl.srw.billcalculator;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;

import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import hugo.weaving.DebugLog;
import pl.srw.billcalculator.preference.PgeSettingsFragment;
import pl.srw.billcalculator.preference.ProviderSettingsActivity;
import pl.srw.billcalculator.util.Dates;

/**
 * Created by Kamil Seweryn
 */
public class MainActivity extends Activity {

    public static final String TAG = "MainActivity";
    public static final int IMAGE_TYPE_KEY = 1109171014;
    public static final String IMAGE_TYPE_STRING = "" + IMAGE_TYPE_KEY;

    public static final String READING_FROM = "READING_FROM";
    public static final String READING_TO = "READING_TO";
    public static final String READING_DAY_FROM = "READING_DAY_FROM";
    public static final String READING_DAY_TO = "READING_DAY_TO";
    public static final String READING_NIGHT_FROM = "READING_NIGHT_FROM";
    public static final String READING_NIGHT_TO = "READING_NIGHT_TO";
    public static final String DATE_FROM = "DATE_FROM";
    public static final String DATE_TO = "DATE_TO";
    public static final String SHARED_PREFERENCES_FILE = "PreferencesFile";
    public static final String PREFERENCE_KEY_FIRST_LAUNCH = "first_launch";

    @InjectView(R.id.button_bill_type_switch) ImageButton bBillType;
    @InjectView(R.id.linearLayout_tariff) LinearLayout llTariff;
    @InjectView(R.id.textView_tariff) TextView tvTariff;
    @InjectView(R.id.textView_tariff_change) TextView tvTariffChange;
    
    @InjectView(R.id.linearLayout_readings) LinearLayout llReadingG11;
    @InjectView(R.id.editText_reading_from) EditText etPreviousReading;
    @InjectView(R.id.editText_reading_to) EditText etCurrentReading;

    @InjectView(R.id.tableLayout_G12_readings) TableLayout tlReadingsG12;
    @InjectView(R.id.editText_reading_day_from) EditText etDayPreviousReading;
    @InjectView(R.id.editText_reading_day_to) EditText etDayCurrentReading;
    @InjectView(R.id.editText_reading_night_from) EditText etNightPreviousReading;
    @InjectView(R.id.editText_reading_night_to) EditText etNightCurrentReading;

    @InjectView(R.id.button_date_from) Button bFromDate;
    @InjectView(R.id.button_date_to) Button bToDate;
    @InjectView(R.id.editText_date_to_error) TextView etToDateError;
    private CheckPricesDialogFragment checkPricesDialog;

    @DebugLog
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        initValues();
    }

    private void initValues() {
        initDates();
        initBillType();
        makeLinkOnTariffChange();
    }

    private void initDates() {
        bFromDate.setText(Dates.format(Dates.firstDayOfThisMonth()));
        bToDate.setText(Dates.format(Dates.lastDayOfThisMonth()));
    }

    private void initBillType() {
        changeBillType(BillType.PGE);
    }

    private void makeLinkOnTariffChange() {
        tvTariffChange.setText(Html.fromHtml(getString(R.string.tariff_change)));
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(IMAGE_TYPE_STRING, getBillType());
    }

    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        changeBillType(restoreBillTypeFrom(savedInstanceState));
        chooseReadings();
        setReadingsHint();
        showPgeTariffLabel();
    }

    private BillType restoreBillTypeFrom(final Bundle state) {
        return (BillType) state.getSerializable(IMAGE_TYPE_STRING);
    }

    @Override
    protected void onStart() {
        super.onStart();
        chooseReadings();
        setTariffLabel();
    }

    private void chooseReadings() {
        final boolean shouldShowDoubleReadings = getBillType() == BillType.PGE
                && isPgeTariffG12();
        if (shouldShowDoubleReadings) {
            llReadingG11.setVisibility(View.GONE);
            tlReadingsG12.setVisibility(View.VISIBLE);
        } else {
            llReadingG11.setVisibility(View.VISIBLE);
            tlReadingsG12.setVisibility(View.GONE);
        }
    }

    private boolean isPgeTariffG12() {
        return PreferenceManager.getDefaultSharedPreferences(this)
                .getString(getString(R.string.preferences_pge_tariff), "").equals(PgeSettingsFragment.TARIFF_G12);
    }

    private void setTariffLabel() {
        if (isPgeTariffG12()) {
            tvTariff.setText(R.string.pge_tariff_G12_on_bill);
        } else {
            tvTariff.setText(R.string.pge_tariff_G11_on_bill);
        }
    }

    @DebugLog
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

    @DebugLog
    private void showCheckPricesDialog() {
        if (checkPricesDialog == null) {
            Log.d(TAG, "need to create new Check Prices dialog");
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
    public void switchBillType() {
        if (getBillType() == BillType.PGE) {
            changeBillType(BillType.PGNIG);
        } else {
            changeBillType(BillType.PGE);
        }
        chooseReadings();
        setReadingsHint();
        showPgeTariffLabel();
        forceRequestFocusOnLayoutChange();
    }

    private void forceRequestFocusOnLayoutChange() {
        // BUG fix for auto focusing 'dateTo error' edit text on hiding G11 reading
        if (isPgeTariffG12()) { // if readings layout changes
            if (getBillType() == BillType.PGE)
                etDayPreviousReading.requestFocus();
            else
                etPreviousReading.requestFocus();
        }
    }

    private void setReadingsHint() {
        if (getBillType() == BillType.PGE) {
            etCurrentReading.setHint(R.string.reading_hint_kWh);
            etPreviousReading.setHint(R.string.reading_hint_kWh);
        } else {
            etCurrentReading.setHint(R.string.reading_hint_m3);
            etPreviousReading.setHint(R.string.reading_hint_m3);
        }
    }

    private void showPgeTariffLabel() {
        if (getBillType() == BillType.PGE) {
            llTariff.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.BounceIn)
                    .duration(400)
                    .playOn(llTariff);
        } else {
            llTariff.setVisibility(View.INVISIBLE);
        }
    }

    private BillType getBillType() {
        return (BillType) bBillType.getTag(IMAGE_TYPE_KEY);
    }

    private void changeBillType(BillType newType) {
        bBillType.setImageResource(newType.drawableId);
        bBillType.setTag(IMAGE_TYPE_KEY, newType);
        YoYo.with(Techniques.BounceIn)
                .duration(400)
                .playOn(bBillType);
    }
    
    @OnClick(R.id.textView_tariff_change)
    public void moveToChangeTariff() {
        final Intent intent = ProviderSettingsActivity
                .createIntent(this, ProviderSettingsActivity.Provider.PGE);
        startActivity(intent);
    }

    @OnClick({R.id.button_date_from, R.id.button_date_to})
    public void showDatePicker(final Button datePickerButton) {
        final LocalDate date = Dates.parse(datePickerButton.getText().toString());
        final int year = date.getYear();
        final int monthValue = date.getMonthValue() - 1;
        final int dayOfMonth = date.getDayOfMonth();

        new DatePickerDialog(MainActivity.this, onDatePickedListener(datePickerButton), year, monthValue, dayOfMonth).show();
    }

    private DatePickerDialog.OnDateSetListener onDatePickedListener(final Button button) {
        return new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                button.setText(Dates.format(year, Month.of(month + 1), day));
                etToDateError.setError(null);
            }
        };
    }

    @DebugLog
    @OnClick(R.id.button_calculate)
    public void calculate() {
        if (!validateForm())
            return;

        Intent billResult = newBillIntent();
        fillParameters(billResult);
        startActivity(billResult);
    }

    private boolean validateForm() {
        return valideteReadings() && validateDates();
    }

    private boolean valideteReadings() {
        if (isSingleReadingsVisible())
            return validateReadingsG11();
        else
            return validateReadingsG12();
    }

    private boolean isSingleReadingsVisible() {
        return llReadingG11.getVisibility() == View.VISIBLE;
    }

    private boolean validateReadingsG11() {
        return validateMissingValue(etPreviousReading) && validateMissingValue(etCurrentReading)
                && validateValueOrder(etPreviousReading, etCurrentReading);
    }

    private boolean validateReadingsG12() {
        return validateMissingValue(etDayPreviousReading) && validateMissingValue(etDayCurrentReading)
                && validateMissingValue(etNightPreviousReading) && validateMissingValue(etNightCurrentReading)
                && validateValueOrder(etDayPreviousReading, etDayCurrentReading) && validateValueOrder(etNightPreviousReading, etNightCurrentReading);
    }

    private boolean validateMissingValue(final EditText et) {
        if (et.getText().toString().isEmpty()) {
            shake(et);
            showError(et, R.string.reading_missing);
            return false;
        }
        return true;
    }

    private boolean validateValueOrder(final EditText prev, final EditText curr) {
        if (!(Integer.parseInt(curr.getText().toString()) > Integer.parseInt(prev.getText().toString()))) {
            shake(curr);
            showError(curr, R.string.reading_order_error);
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
        LocalDate from = Dates.parse(fromDate);
        LocalDate to = Dates.parse(toDate);
        if (!from.isBefore(to)) {
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

    private Intent newBillIntent() {
        if (getBillType() == BillType.PGE) {
            return new Intent(this, EnergyBillActivity.class);
        } else {
            return new Intent(this, GasBillActivity.class);
        }
    }

    private void fillParameters(Intent billResult) {
        if (isSingleReadingsVisible()) {
            putIntExtra(billResult, READING_FROM, etPreviousReading);
            putIntExtra(billResult, READING_TO, etCurrentReading);
        } else {
            putIntExtra(billResult, READING_DAY_FROM, etDayPreviousReading);
            putIntExtra(billResult, READING_DAY_TO, etDayCurrentReading);

            putIntExtra(billResult, READING_NIGHT_FROM, etNightPreviousReading);
            putIntExtra(billResult, READING_NIGHT_TO, etNightCurrentReading);
        }

        putStringExtra(billResult, DATE_FROM, bFromDate);
        putStringExtra(billResult, DATE_TO, bToDate);
    }

    private void putStringExtra(Intent intent, String key, TextView valueView) {
        String fromDate = valueView.getText().toString();
        intent.putExtra(key, fromDate);
    }

    private void putIntExtra(Intent intent, String key, TextView valueView) {
        String prev = valueView.getText().toString();
        intent.putExtra(key, Integer.parseInt(prev));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startSettings();
            return true;
        } else if (item.getItemId() == R.id.action_about) {
            startAbout();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
