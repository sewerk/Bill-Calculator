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
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends Activity {

    public static final String READING_FROM = "READING_FROM";
    public static final String READING_TO = "READING_TO";
    public static final String DATE_FROM = "DATE_FROM";
    public static final String DATE_TO = "DATE_TO";

    private EditText etPreviousReading;
    private EditText etCurrentReading;
    private ToggleButton bBillType;
    private Button bFromDate;
    private Button bToDate;
    private TextView tvForPeriod;
    private Button bCalculate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bBillType = (ToggleButton) findViewById(R.id.button_bill_type_switch);
        etPreviousReading = (EditText) findViewById(R.id.editText_from);
        etCurrentReading = (EditText) findViewById(R.id.editText_to);
        tvForPeriod = (TextView) findViewById(R.id.textView_za_okres);
        bFromDate = (Button) findViewById(R.id.button_from);
        bToDate = (Button) findViewById(R.id.button_to);
        bCalculate = (Button) findViewById(R.id.button_calculate);

        onDateButtonClicked(bFromDate);
        onDateButtonClicked(bToDate);
        onCalculateClicked();
    }

    private void onDateButtonClicked(final Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                new DatePickerDialog(MainActivity.this, getOnDateSetListener(button), year, month, day).show();
            }

            private DatePickerDialog.OnDateSetListener getOnDateSetListener(final Button button) {
                return new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        button.setText(new StringBuilder().append(day)
                                .append("/").append(month + 1).append("/").append(year)
                                .append(" "));
                        tvForPeriod.setError(null);
                    }
                };
            }
        });
    }

    private void onCalculateClicked() {
        bCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateForm())
                    return;

                Intent billResult = getIntent(bBillType);
                fillParameters(billResult);
                startActivity(billResult);
            }
        });
    }

    private boolean validateForm() {
        if (!valideteReadings()) {
            etCurrentReading.setError(getString(R.string.reading_error));
            showKeyboard(etCurrentReading);
            return false;
        }
        if (!validateDates()) {
            tvForPeriod.setError(getString(R.string.date_error));
            return false;
        }
        return true;
    }

    private boolean valideteReadings() {
        String prev = etPreviousReading.getText().toString();
        String current = etCurrentReading.getText().toString();
        return !prev.isEmpty() && !current.isEmpty() && Integer.parseInt(current) > Integer.parseInt(prev);
    }

    private boolean validateDates() {
        String fromDate = bFromDate.getText().toString();
        String toDate = bToDate.getText().toString();
        if (fromDate.length() < 8 || toDate.length() < 8)
            return false;
        SimpleDateFormat formater = new SimpleDateFormat("d/MM/yyyy");
        try {
            Date from = formater.parse(fromDate);
            Date to = formater.parse(toDate);
            return from.before(to);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void showKeyboard(TextView mTextView) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            // only will trigger it if no physical keyboard is open
            imm.showSoftInput(mTextView, 0);
        }
    }

    private Intent getIntent(ToggleButton bBillType) {
        if (!bBillType.isChecked()) {
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
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }
}
