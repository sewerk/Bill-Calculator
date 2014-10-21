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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends Activity {

    private static final int IMAGE_TYPE_KEY = 1109171014;

    public static final String READING_FROM = "READING_FROM";
    public static final String READING_TO = "READING_TO";
    public static final String DATE_FROM = "DATE_FROM";
    public static final String DATE_TO = "DATE_TO";
    public static final String DATE_PATTERN = "dd/MM/yyyy";

    private EditText etPreviousReading;
    private EditText etCurrentReading;
    private ImageButton bBillType;
    private Button bFromDate;
    private Button bToDate;
    private TextView tvForPeriod;
    private Button bCalculate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bBillType = (ImageButton) findViewById(R.id.button_bill_type_switch);
        etPreviousReading = (EditText) findViewById(R.id.editText_from);
        etCurrentReading = (EditText) findViewById(R.id.editText_to);
        tvForPeriod = (TextView) findViewById(R.id.textView_za_okres);
        bFromDate = (Button) findViewById(R.id.button_from);
        bToDate = (Button) findViewById(R.id.button_to);
        bCalculate = (Button) findViewById(R.id.button_calculate);

        setDefaultDates();
        onImageButtonClicked();
        onDateButtonClicked(bFromDate);
        onDateButtonClicked(bToDate);
        onCalculateClicked();
    }

    private void setDefaultDates() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);

        bFromDate.setText(buildDateString(year, month, 1));
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.add(Calendar.MONTH, 1);
        c.add(Calendar.DAY_OF_MONTH, -1);
        bToDate.setText(buildDateString(year, month, c.get(Calendar.DAY_OF_MONTH)));
    }

    private void onImageButtonClicked() {
        bBillType.setTag(IMAGE_TYPE_KEY, R.drawable.bill_type_pge);
        bBillType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((Integer) bBillType.getTag(IMAGE_TYPE_KEY)) == R.drawable.bill_type_pge) {
                    bBillType.setBackgroundResource(R.drawable.bill_type_pgnig);
                    bBillType.setTag(IMAGE_TYPE_KEY, R.drawable.bill_type_pgnig);
                } else {
                    bBillType.setBackgroundResource(R.drawable.bill_type_pge);
                    bBillType.setTag(IMAGE_TYPE_KEY, R.drawable.bill_type_pge);
                }
            }
        });
    }

    private void onDateButtonClicked(final Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                c.setTime(readDateFrom(button));
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                new DatePickerDialog(MainActivity.this, getOnDateSetListener(button), year, month, day).show();
            }

            private Date readDateFrom(final Button button) {
                SimpleDateFormat dateParser = new SimpleDateFormat(DATE_PATTERN);
                try {
                    return dateParser.parse(button.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                    return new Date();
                }
            }

            private DatePickerDialog.OnDateSetListener getOnDateSetListener(final Button button) {
                return new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        button.setText(buildDateString(year, month, day));
                        tvForPeriod.setError(null);
                    }
                };
            }
        });
    }

    private String buildDateString(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        return new SimpleDateFormat(DATE_PATTERN).format(c.getTime());
    }

    private void onCalculateClicked() {
        bCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateForm())
                    return;

                Intent billResult = newIntentBy(bBillType);
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
        SimpleDateFormat formater = new SimpleDateFormat(DATE_PATTERN);
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

    private Intent newIntentBy(ImageButton bBillType) {
        if (bBillType.getTag(IMAGE_TYPE_KEY).equals(R.drawable.bill_type_pge)) {
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
